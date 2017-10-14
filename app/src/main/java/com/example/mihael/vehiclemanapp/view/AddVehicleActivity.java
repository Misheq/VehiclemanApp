package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddVehicleActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
    }

    private void saveVehicle(Vehicle vehicle) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // creates vehicle without person
        List<Vehicle> vehicleList = new ArrayList<Vehicle>();
        vehicleList.add(vehicle);

        /*
        Person dummyPerson = new Person();
        dummyPerson.setPersonId(0);
        dummyPerson.setFirstName("AAAA");
        dummyPerson.setLastName("BBBB");
        dummyPerson.setEmail("sdsdsdsd");
        */

        PersonVehicleMapper pvm = new PersonVehicleMapper(new Person(), vehicleList); // maybe not allowed because person has vehicle list

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

        Vehicle vehicle = new Vehicle();
        vehicle.setVehicleType(type.getText().toString());
        vehicle.setRegistrationNumber(registration.getText().toString());

        saveVehicle(vehicle);
    }
}
