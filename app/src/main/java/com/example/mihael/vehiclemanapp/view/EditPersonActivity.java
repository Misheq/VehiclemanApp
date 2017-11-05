package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
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

import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_VEHICLE;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        Intent intent = getIntent();

        final Person person = (Person) intent.getSerializableExtra("person");
        passedPersonId = person.getPersonId();

        int currentVehicleId = -1;
        if(person.getVehicles().size() != 0) {
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

    private void setFieldsFromPerson(Person p) {
        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);

        spinnerWithVehicles = spinnerLoader.getVehiclesSpinner();

        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        email.setText(p.getEmail());

        if(!p.getPhone().equals("")) {
            phone.setText(p.getPhone());
        }

        if(!p.getCompanyName().equals("")) {
            company.setText(p.getCompanyName());
        }

        int vehiclePosition = 0;

        if(!p.getVehicles().isEmpty()) {
            List<Vehicle> vehicleList = spinnerLoader.getVehicles();
            for(int i = 1; i < vehicleList.size(); i++) {
                if(vehicleList.get(i).getVehicleId() == p.getVehicles().get(0).getVehicleId()) {
                    vehiclePosition = i;
                    break;
                }
            }
            spinnerWithVehicles.setSelection(vehiclePosition);
        }
    }

    public void setPersonFromForm(View view) {

        Person person = new Person();
        person.setPersonId(passedPersonId);

        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);
        spinnerWithVehicles = findViewById(R.id.spinnerVehicles);

        Object selectedVehicle = spinnerWithVehicles.getSelectedItem();
        List<Vehicle> vehList = new ArrayList<>();

        if(!selectedVehicle.toString().equals(SELECT_VEHICLE)) {
            Vehicle vehicle = (Vehicle) selectedVehicle;
            vehList.add(vehicle);
            person.setVehicles(vehList);
        } else {
            person.setVehicles(vehList);
        }

        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isPersonInputValid()) {
            person.setFirstName(firstName.getText().toString());
            person.setLastName(lastName.getText().toString());
            person.setEmail(email.getText().toString());
            person.setPhone(phone.getText().toString());
            person.setCompanyName(company.getText().toString());

            Log.d("UPDATE", "Person updated");

            // will call update to server
            editPerson(person);
        }
    }

    private void editPerson(Person person) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Person> call = apiInterface.updatePerson(LoginManager.getLogedInManagerToken(), person.getPersonId(), person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson updated successfully!");
                    Toast.makeText(EditPersonActivity.this, "Person updated", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson not updated!");
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
                Log.d("MY_ERROR", "something is wrong with person update");
            }
        });
    }
}
