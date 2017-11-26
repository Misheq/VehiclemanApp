package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;
import com.example.mihael.vehiclemanapp.helpers.SpinnerLoader;
import com.example.mihael.vehiclemanapp.interfaces.SpinnerEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CONNECTION_FAILED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_VEHICLE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.UPDATE_PERSON_SUCCESSFUL;

/**
 * Activity which is responsible for editing existing persons
 */

public class EditPersonActivity extends AppCompatActivity {

    private int passedPersonId;
    private View view;
    private SpinnerLoader spinnerLoader;
    private Spinner spinnerWithVehicles;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText company;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        final Person person = (Person) intent.getSerializableExtra("person");
        passedPersonId = person.getPersonId();

        int currentVehicleId = -1;
        if (person.getVehicles().size() != 0) {
            currentVehicleId = person.getVehicles().get(0).getVehicleId();
        }

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, currentVehicleId);
        spinnerLoader.loadVehiclesSpinnerForPerson();
        spinnerLoader.setEventListener(new SpinnerEventListener() {
            @Override
            public void onEventOccured() {
                setFieldsFromPerson(person);
            }
        });
    }

    private void setFieldsFromPerson(Person person) {
        setUiFromPerson(person);
        setSpinnerVehicle(person);
    }

    private void setPersonFromForm() {

        InputValidator inVal = new InputValidator(this.view);

        if (inVal.isPersonInputValid()) {
            Person person = createPersonFromUi();
            person = setVehicleForPerson(person);

            // will call update to server
            editPerson(person);
        }
    }

    private void editPerson(Person person) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Person> call = apiInterface.updatePerson(LoginManager.getLogedInManagerToken(), person.getPersonId(), person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 200) {
                    Toast.makeText(EditPersonActivity.this, UPDATE_PERSON_SUCCESSFUL, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(EditPersonActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(EditPersonActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUiElements() {
        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);
    }

    private Person createPersonFromUi() {
        getUiElements();

        Person person = new Person();

        person.setPersonId(passedPersonId);
        person.setFirstName(firstName.getText().toString());
        person.setLastName(lastName.getText().toString());
        person.setEmail(email.getText().toString());
        person.setPhone(phone.getText().toString());
        person.setCompanyName(company.getText().toString());
        person.setManagerId(LoginManager.getManagerId());

        return person;
    }

    private Person setVehicleForPerson(Person person) {
        Object selectedVehicle = spinnerWithVehicles.getSelectedItem();
        List<Vehicle> vehList = new ArrayList<>();

        if (!selectedVehicle.toString().equals(SELECT_VEHICLE)) {
            Vehicle vehicle = (Vehicle) selectedVehicle;
            vehList.add(vehicle);
            person.setVehicles(vehList);
        } else {
            person.setVehicles(vehList);
        }

        return person;
    }

    private void setUiFromPerson(Person person) {
        getUiElements();

        spinnerWithVehicles = spinnerLoader.getVehiclesSpinner();
        firstName.setText(person.getFirstName());
        lastName.setText(person.getLastName());
        email.setText(person.getEmail());
        phone.setText(person.getPhone());
        company.setText(person.getCompanyName());

        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    private void setSpinnerVehicle(Person person) {
        int vehiclePosition = 0;

        if (!person.getVehicles().isEmpty()) {
            List<Vehicle> vehicleList = spinnerLoader.getVehicles();
            for (int i = 1; i < vehicleList.size(); i++) {
                if (vehicleList.get(i).getVehicleId() == person.getVehicles().get(0).getVehicleId()) {
                    vehiclePosition = i;
                    break;
                }
            }
            spinnerWithVehicles.setSelection(vehiclePosition);
        }
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.SAVE);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            setPersonFromForm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
