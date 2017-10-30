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
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditVehicleActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private Spinner personSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        Intent intent = getIntent();

        Vehicle vehicle = (Vehicle) intent.getSerializableExtra("vehicle");
        setFieldsFromVehicle(vehicle);
    }

    private void setFieldsFromVehicle(Vehicle vehicle) {
        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);
        Spinner vehicleSpinner = findViewById(R.id.spinnerPersons);

        type.setText(vehicle.getVehicleType());
        registration.setText(vehicle.getRegistrationNumber());

        if(!(vehicle.getPerson() == null)) {
            // select the vehicle.toString() to be displayed
        }
    }

    /**
     * on save button clicked:
     * create the vehicle object and
     * send update request for the vehicle
     *
     * @param view
     */
    public void setVehicleFromForm(View view) {

        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);

        InputValidator inVal = new InputValidator(getWindow().getDecorView().getRootView());

        if(inVal.isVehicleInputValid()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleType(type.getText().toString());
            vehicle.setRegistrationNumber(registration.getText().toString());

            Log.d("UPDATE", "Vehicle updated");

            // will call update to server
            // editVehicle(vehicle);
        }
    }

    private void editVehicle(Vehicle vehicle) {

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

        Call<PersonVehicleMapper> call = apiInterface.updateVehicle(vehicle.getVehicleId(), pvm);
        call.enqueue(new Callback<PersonVehicleMapper>() {
            @Override
            public void onResponse(Call<PersonVehicleMapper> call, Response<PersonVehicleMapper> response) {
                int statusCode = response.code();

                if(statusCode == 204) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nVehicle updated successfully!");
                    Toast.makeText(EditVehicleActivity.this, "Vehicle updated", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nVehicle not updated!");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(EditVehicleActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonVehicleMapper> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong with vehicle update");
            }
        });
    }
}
