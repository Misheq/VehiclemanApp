package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void setManagerFromForm(View view) {
        Manager manager = new Manager();

        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);
        EditText email = findViewById(R.id.inputEmail);
        EditText password = findViewById(R.id.inputPassword);
        EditText confirmPassword = findViewById(R.id.inputConfirmPassword);

        // how to send password to backend?


        InputValidator inVal = new InputValidator(view);

        // manager and person input is identical (for the required fields)
        if(inVal.isPersonInputValid() && inVal.isPasswordValid()) {
            manager.setFirstName(firstName.getText().toString());
            manager.setLastName(lastName.getText().toString());
            manager.setPhone(phone.getText().toString());
            manager.setCompanyName(company.getText().toString());
            manager.setEmail(email.getText().toString());

            Log.d("CREATE", "Manager created");

            // will call update to server
            // registerManager(manager);
        }

    }
}
