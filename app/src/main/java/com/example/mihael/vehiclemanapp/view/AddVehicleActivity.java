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
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.SpinnerLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_PERSON;

/**
 * Activity which is responsible for creating vehicles
 */

public class AddVehicleActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private View view;
    private SpinnerLoader spinnerLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, -1);
        spinnerLoader.loadPersonsSpinnerForVehicle();
    }

    public void setVehicleFromForm(View view) {
        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);
        Spinner personsSpinner = findViewById(R.id.spinnerPersons);

        Object selectedPerson = personsSpinner.getSelectedItem();
        Person person;

        Vehicle vehicle = new Vehicle();

        if(!selectedPerson.toString().equals(SELECT_PERSON)) {
            person = (Person) selectedPerson;
            vehicle.setPerson(person);
            vehicle.setAssigneeId(String.valueOf(person.getPersonId()));
        } else {
            person = null;
        }

        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isVehicleInputValid()){
            vehicle.setVehicleType(type.getText().toString());
            vehicle.setRegistrationNumber(registration.getText().toString());

            saveVehicle(vehicle);
        }
    }

    private void saveVehicle(Vehicle vehicle) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Vehicle> call = apiInterface.createVehicle(vehicle);
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
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
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong with vehicle create");
            }
        });
    }
}
