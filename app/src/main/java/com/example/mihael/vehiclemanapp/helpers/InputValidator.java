package com.example.mihael.vehiclemanapp.helpers;

import android.content.Context;
import android.renderscript.ScriptGroup;
import android.view.View;
import android.widget.EditText;

import com.example.mihael.vehiclemanapp.R;

/**
 * Created by mihae on 2017. 10. 28..
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
            firstName.setError("First name is required!");
            isValid = false;
        }

        if(lastName.getText().toString().length() == 0) {
            lastName.setError("Last name is required!");
            isValid = false;
        }

        if(email.getText().toString().length() == 0) {
            email.setError("Email is required!");
            isValid = false;
        }

        return isValid;
    }

    public boolean isVehicleInputValid() {
        EditText type = view.findViewById(R.id.inputType);
        EditText registration = view.findViewById(R.id.inputRegistration);

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
