package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
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
import com.example.mihael.vehiclemanapp.interfaces.SpinnerEventListener;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_PERSON;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLE;

/**
 * Activity which is responsible for editing existing vehicles
 */

public class EditVehicleActivity extends AppCompatActivity {

    private Spinner spinnerWithPersons;
    private View view;
    private SpinnerLoader spinnerLoader;
    private EditText type;
    private EditText registration;
    private EditText color;
    private EditText description;
    //private DatePicker servicingDate;

    private int passedVehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        Intent intent = getIntent();
        final Vehicle vehicle = (Vehicle) intent.getSerializableExtra(VEHICLE);
        passedVehicleId = vehicle.getVehicleId();

        int currentPersonId = -1;
        if(!vehicle.getAssigneeId().equals("")) {
            currentPersonId = Integer.parseInt(vehicle.getAssigneeId());
        }

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, currentPersonId);
        spinnerLoader.loadPersonsSpinnerForVehicle();
        spinnerLoader.setEventListener(new SpinnerEventListener() {
            @Override
            public void onEventOccured() {
                setFieldsFromVehicle(vehicle);
            }
        });
    }

    private void setFieldsFromVehicle(Vehicle vehicle) {
        setUiTextFromVehicle(vehicle);
        setSpinnerPerson(vehicle);
    }

    /**
     * on save button clicked:
     * create the vehicle object and
     * send update request for the vehicle
     *
     * @param view
     */
    public void setVehicleFromForm(View view) {
        InputValidator inVal = new InputValidator(this.view);
        if(inVal.isVehicleInputValid()) {

            Vehicle vehicle = createVehicleFromUi();
            vehicle = setPersonForVehicle(vehicle);

            // will call update to server
            editVehicle(vehicle);
        }
    }

    private void editVehicle(Vehicle vehicle) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Vehicle> call = apiInterface.updateVehicle(LoginManager.getLogedInManagerToken(),vehicle.getVehicleId(), vehicle);
        call.enqueue(new Callback<Vehicle>() {
            @Override
            public void onResponse(Call<Vehicle> call, Response<Vehicle> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
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
            public void onFailure(Call<Vehicle> call, Throwable t) {
                Toast.makeText(EditVehicleActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.d("MY_ERROR", "something is wrong with vehicle update");
            }
        });
    }

    ////////////// helpers /////////////////

    private void getUiElements() {
        type = findViewById(R.id.inputType);
        registration = findViewById(R.id.inputRegistration);
        spinnerWithPersons = findViewById(R.id.spinnerPersons);
        color = findViewById(R.id.inputColor);
        description = findViewById(R.id.inputDescription);
        //servicingDate = findViewById(R.id.inputServicingDate);
    }

    private void setUiTextFromVehicle(Vehicle vehicle) {
        getUiElements();

        type.setText(vehicle.getVehicleType());
        registration.setText(vehicle.getRegistrationNumber());
        color.setText(vehicle.getColor());
        description.setText(vehicle.getDescription());
    }

    private void setSpinnerPerson(Vehicle vehicle) {
        int personPosition = 0;

        if(!(vehicle.getAssigneeId().equals(""))) {

            List<Person> personList = spinnerLoader.getPersons();
            for(int i = 0; i < personList.size(); i++) {
                if (personList.get(i).getPersonId() == Integer.parseInt(vehicle.getAssigneeId())) {
                    personPosition = i;
                    break;
                }
            }
            spinnerWithPersons.setSelection(personPosition);
        }
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

    private Vehicle createVehicleFromUi() {
        getUiElements();

        Vehicle vehicle = new Vehicle();

        vehicle.setVehicleId(passedVehicleId);
        vehicle.setVehicleType(type.getText().toString());
        vehicle.setRegistrationNumber(registration.getText().toString());
        vehicle.setManagerId(LoginManager.getManagerId());
        vehicle.setColor(color.getText().toString());
        vehicle.setDescription(description.getText().toString());
        //vehicle.setServicingDate(servicingDate.toString());

        return vehicle;
    }

}
