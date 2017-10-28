package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;

public class EditVehicleActivity extends AppCompatActivity {

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

        Spinner Vehicle = findViewById(R.id.spinnerPersons);

        type.setText(vehicle.getVehicleType());
        registration.setText(vehicle.getRegistrationNumber());

        if(!(vehicle.getPerson() == null)) {
            // select the vehicle.toString() to be displayed
        }
    }

    public void setVehicleFromForm(View view) {

        EditText type = findViewById(R.id.inputType);
        EditText registration = findViewById(R.id.inputRegistration);

        InputValidator inVal = new InputValidator(getWindow().getDecorView().getRootView());

        if(inVal.isPersonInputValid()) {
            Vehicle vehicle = new Vehicle();
            vehicle.setVehicleType(type.getText().toString());
            vehicle.setRegistrationNumber(registration.getText().toString());

            Log.d("UPDATE", "Vehicle updated");

            // will call update to server
            //updatePerson(person);
        }
    }
}
