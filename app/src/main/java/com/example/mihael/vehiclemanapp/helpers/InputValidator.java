package com.example.mihael.vehiclemanapp.helpers;

import android.view.View;
import android.widget.EditText;

import com.example.mihael.vehiclemanapp.R;

import static com.example.mihael.vehiclemanapp.helpers.Constants.EMAIL_FORMAT_INVALID;
import static com.example.mihael.vehiclemanapp.helpers.Constants.FIRST_NAME_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.LAST_NAME_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.PASSWORD_CHARACTERS;
import static com.example.mihael.vehiclemanapp.helpers.Constants.PASSWORD_NOT_MATCH;
import static com.example.mihael.vehiclemanapp.helpers.Constants.PASSWORD_TOO_SHORT;
import static com.example.mihael.vehiclemanapp.helpers.Constants.REG_NUM_REQUIRED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLE_TYPE_REQUIRED;

/**
 * UI input check for vehicle and person input to reduce errors
 * The class checks if all necessary fields are entered
 * and alerts the user if something is missing
 */

public class InputValidator {

    private View view;
    private final int minLength = 6;

    public InputValidator(View view) {
        this.view = view;
    }

    public boolean isRegistrationPasswordValid() {
        EditText password = view.findViewById(R.id.inputPassword);
        EditText confirmPassword = view.findViewById(R.id.inputConfirmPassword);

        boolean isValid = true;

        if(confirmPassword.length() < minLength) {
            confirmPassword.setError(PASSWORD_TOO_SHORT + minLength + PASSWORD_CHARACTERS);
            isValid = false;
        }

        if(!isPasswordValid()) {
            isValid = false;
        }

        if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
            confirmPassword.setError(PASSWORD_NOT_MATCH);
            isValid = false;
        }

        return isValid;
    }

    public boolean isPasswordValid() {
        EditText password = view.findViewById(R.id.inputPassword);

        boolean isValid = true;

        if(password.length() < minLength) {
            password.setError(PASSWORD_TOO_SHORT + minLength + PASSWORD_CHARACTERS);
            isValid = false;
        }

        return isValid;
    }

    public boolean isEmailValid() {
        EditText email = view.findViewById(R.id.inputEmail);

        boolean isValid = true;

        if(email.length() < 6 || !email.getText().toString().contains("@") || !email.getText().toString().contains(".")) {
            email.setError(EMAIL_FORMAT_INVALID);
            isValid = false;
        }

        return isValid;
    }

    public boolean isLoginValid() {
        boolean isValid = true;

        if(!isEmailValid()) {
            isValid = false;
        }

        if(!isPasswordValid()) {
            isValid = false;
        }

        return isValid;
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

        if (!isEmailValid()) {
            isValid = false;
        }

        if(!email.getText().toString().contains("@")) {
            email.setError(EMAIL_FORMAT_INVALID);
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
