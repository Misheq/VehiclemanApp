package com.example.mihael.vehiclemanapp.helpers;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.EditText;

import com.example.mihael.vehiclemanapp.R;

import static com.example.mihael.vehiclemanapp.helpers.Constants.EMAIL_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.FIRST_NAME_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.LAST_NAME_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.REG_NUM_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLE_TYPE_REQUIRED;

/**
 * UI input check for vehicle and person input to reduce errors
 * The class checks if all necessary fields are entered
 * and alerts the user if something is missing
 */

public class InputValidator {

    private View view;

    public InputValidator(View view) {
        this.view = view;
    }

    public boolean isPersonInputValid() {
        EditText firstName = view.findViewById(R.id.inputFirstName);
        EditText lastName = view.findViewById(R.id.inputLastName);
        EditText email = view.findViewById(R.id.inputEmail);

        boolean isValid = true;

        if(firstName.getText().toString().length() == 0) {
            firstName.setError(FIRST_NAME_REQUIRED);
            isValid = false;
        }

        if(lastName.getText().toString().length() == 0) {
            lastName.setError(LAST_NAME_REQUIRED);
            isValid = false;
        }

        if(email.getText().toString().length() == 0) {
            email.setError(EMAIL_REQUIRED);
            isValid = false;
        }

        return isValid;
    }

    public boolean isVehicleInputValid() {
        EditText type = view.findViewById(R.id.inputType);
        EditText registration = view.findViewById(R.id.inputRegistration);

        boolean isInputValid = true;

        if(type.getText().toString().length() == 0) {
            type.setError(VEHICLE_TYPE_REQUIRED);
            isInputValid = false;
        }

        if(registration.getText().toString().length() == 0) {
            registration.setError(REG_NUM_REQUIRED);
            isInputValid = false;
        }

        return isInputValid;
    }
}
