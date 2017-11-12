package com.example.mihael.vehiclemanapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONObject;

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
    EditText type;
    EditText registration;
    EditText color;
    EditText description;
    // DatePicker servicingDate;
    Spinner spinnerWithPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, -1);
        spinnerLoader.loadPersonsSpinnerForVehicle();
    }

    public void setVehicleFromForm(View view) {
        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isVehicleInputValid()){
            Vehicle vehicle = createVehicleFromUi();
            vehicle = setPersonForVehicle(vehicle);

            saveVehicle(vehicle);
        }
    }

    private void saveVehicle(Vehicle vehicle) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Vehicle> call = apiInterface.createVehicle(LoginManager.getLogedInManagerToken(), vehicle);
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
                Toast.makeText(AddVehicleActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.d("MY_ERROR", "something is wrong with vehicle create");
            }
        });
    }

    ////////////// helpers //////////////

    private void getUiElements() {
        type = findViewById(R.id.inputType);
        registration = findViewById(R.id.inputRegistration);
        color = findViewById(R.id.inputColor);
        description = findViewById(R.id.inputDescription);
        // servicingDate = findViewById(R.id.inputServicingDate);
        spinnerWithPersons = findViewById(R.id.spinnerPersons);
    }

    private Vehicle createVehicleFromUi() {
        getUiElements();

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleType(type.getText().toString());
        vehicle.setRegistrationNumber(registration.getText().toString());
        vehicle.setManagerId(LoginManager.getManagerId());
        vehicle.setColor(color.getText().toString());
        vehicle.setDescription(description.getText().toString());
        //vehicle.setServicingDate(servicingDate.toString());

        return vehicle;
    }

    private Vehicle setPersonForVehicle(Vehicle vehicle) {
        Object selectedPerson = spinnerWithPersons.getSelectedItem();

        if(!selectedPerson.toString().equals(SELECT_PERSON)) {
            Person person = (Person) selectedPerson;
            vehicle.setAssigneeId(String.valueOf(person.getPersonId()));
            vehicle.setPerson(person);
        } else {
            vehicle.setAssigneeId("");
        }

        return vehicle;
    }
}
