package com.example.mihael.vehiclemanapp.view;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CREATE_PERSON_SUCCESSFUL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.CREATE_VEHICLE_SUCCESSFUL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_VEHICLE;

/**
 * Activity which is responsible for creating persons
 */

public class AddPersonActivity extends AppCompatActivity {

    private View view;
    private SpinnerLoader spinnerLoader;
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText company;
    private Spinner spinnerWithVehicles;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, -1);
        spinnerLoader.loadVehiclesSpinnerForPerson();
    }

    private void setPersonFromForm() {
        InputValidator inVal = new InputValidator(this.view);

        if (inVal.isPersonInputValid()) {
            Person person = createPersonFromUi();
            person = setVehicleForPerson(person);

            savePerson(person);
        }
    }

    private void savePerson(Person person) {

        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Person> call = apiInterface.createPerson(LoginManager.getLogedInManagerToken(), person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 201) {
                    Toast.makeText(AddPersonActivity.this, CREATE_PERSON_SUCCESSFUL, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddPersonActivity.this,
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
                Toast.makeText(AddPersonActivity.this, CREATE_VEHICLE_SUCCESSFUL, Toast.LENGTH_LONG).show();
            }
        });
    }

    /////////////////// helpers ////////////////////

    private void getUiElements() {
        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);
        spinnerWithVehicles = findViewById(R.id.spinnerVehicles);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
    }

    private Person createPersonFromUi() {
        getUiElements();

        Person person = new Person();

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
