package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

public class AddPersonActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private Spinner vehicleSpinner;
    private List<Vehicle> vehicles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        loadSpinnerData();
    }

    private void addVehicleOnSpinner(List<Vehicle> vehicles) {
        vehicleSpinner = findViewById(R.id.spinnerVehicles);
        vehicleSpinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vehiclesToString(vehicles));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehicleSpinner.setAdapter(adapter);
    }

    private List<String> vehiclesToString(List<Vehicle> vehicles) {
        List<String> vehiclesList = new ArrayList<>();

        vehiclesList.add("Select Vehicle");

        for(Vehicle v : vehicles) {
            vehiclesList.add(v.toString());
        }

        return vehiclesList;
    }

    private void loadSpinnerData() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns vehicle list
        Call<List<Vehicle>> call = apiInterface.getVehicles();
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    Log.d("DEBUG", "Get vehicles successful");
                    vehicles = response.body();
                    addVehicleOnSpinner(vehicles);
                    Toast.makeText(AddPersonActivity.this, "Read vehicles status: " + statusCode, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddPersonActivity.this, "Read vehicles status: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }

    private void savePerson(Person person) {

        List<Vehicle> vehicleList = new ArrayList<>();

        /* gets only string from spinner
        Object selectedVehicle = vehicleSpinner.getSelectedItem();

        if(!selectedVehicle.toString().equals("Select Vehicle")) {
            Vehicle v = (Vehicle) selectedVehicle;
            vehicleList.add(v);
        }
        */

        PersonVehicleMapper pvm = new PersonVehicleMapper(person, vehicleList);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<PersonVehicleMapper> call = apiInterface.createPerson(pvm);
        call.enqueue(new Callback<PersonVehicleMapper>() {
            @Override
            public void onResponse(Call<PersonVehicleMapper> call, Response<PersonVehicleMapper> response) {
                int statusCode = response.code();

                if(statusCode == 201) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson created successfully!");
                    Toast.makeText(AddPersonActivity.this, "Person created", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson not created!");
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
            public void onFailure(Call<PersonVehicleMapper> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong");
            }
        });
    }

    public void setPersonFromForm(View view) {

        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText email = findViewById(R.id.inputEmail);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);

        Person person = new Person();
        person.setFirstName(firstName.getText().toString());
        person.setLastName(lastName.getText().toString());
        person.setEmail(email.getText().toString());
        person.setPhone(phone.getText().toString());
        person.setCompanyName(company.getText().toString());

        savePerson(person);
    }
}
