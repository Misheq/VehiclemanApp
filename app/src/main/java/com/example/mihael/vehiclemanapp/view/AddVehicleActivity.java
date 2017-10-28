package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.CustomOnItemSelectedListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private Spinner personSpinner;
    private List<Person> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        loadSpinnerData();
    }

    private void loadSpinnerData() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns persons list
        Call<List<Person>> call = apiInterface.getPersons();
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("DEBUG", "Get persons successful");
                    persons = response.body();
                    addPersonOnSpinner(persons);
                }

                Log.d("DEBUG", "Get persons not successful, status: " + statusCode);
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }

    private void addPersonOnSpinner(List<Person> persons) {
        personSpinner = findViewById(R.id.spinnerPersons);
        personSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        Person dummyPerson = new Person();
        dummyPerson.setFirstName("Select");
        dummyPerson.setLastName("person");
        persons.add(0, dummyPerson);

        ArrayAdapter<Person> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, persons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personSpinner.setAdapter(adapter);
    }

    private void saveVehicle(Vehicle vehicle) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Person person;
        Object selectedPerson = personSpinner.getSelectedItem();

        // if not dummy person, then set person from spinner
        if(!selectedPerson.toString().equals("Select person")) {
            person = (Person) selectedPerson;
        } else {
            person = null;
        }

        // sets vehicle
        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(vehicle);

        PersonVehicleMapper pvm = new PersonVehicleMapper(person, vehicleList);

        Call<PersonVehicleMapper> call = apiInterface.createVehicle(pvm);
        call.enqueue(new Callback<PersonVehicleMapper>() {
            @Override
            public void onResponse(Call<PersonVehicleMapper> call, Response<PersonVehicleMapper> response) {
                int statusCode = response.code();

                if(statusCode == 201) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nVehicle created successfully!");
                    Toast.makeText(AddVehicleActivity.this, "Vehicle created", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nVehicle not created!");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddVehicleActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonVehicleMapper> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong with vehicle");
            }
        });
    }

    public void setVehicleFromForm(View view) {
        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);

        if(isVehicleInputValid()){
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleType(type.getText().toString());
            vehicle.setRegistrationNumber(registration.getText().toString());

            saveVehicle(vehicle);
        }
    }

    private boolean isVehicleInputValid() {
        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);

        boolean isInputValid = true;

        if(type.getText().toString().length() == 0) {
            type.setError("Vehicle type is required!");
            isInputValid = false;
        }

        if(registration.getText().toString().length() == 0) {
            registration.setError("Registration is required!");
            isInputValid = false;
        }

        return isInputValid;
    }
}
