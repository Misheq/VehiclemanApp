package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;

public class EditPersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        Intent intent = getIntent();

        Person person = (Person) intent.getSerializableExtra("person");
        setFieldsFromPerson(person);
    }

    private void setFieldsFromPerson(Person p) {
        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText email = findViewById(R.id.inputEmail);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);
        Spinner vehicle = findViewById(R.id.spinnerVehicles);

        firstName.setText(p.getFirstName());
        lastName.setText(p.getLastName());
        email.setText(p.getEmail());

        if(!p.getPhone().equals("")) {
            phone.setText(p.getPhone());
        }

        if(!p.getCompanyName().equals("")) {
            company.setText(p.getCompanyName());
        }

        if(!p.getVehicles().isEmpty()) {
            // select the vehicle.toString() to be displayed
        }
    }

    public void setPersonFromForm(View view) {

        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText email = findViewById(R.id.inputEmail);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);

        InputValidator inVal = new InputValidator(getWindow().getDecorView().getRootView());

        if(inVal.isPersonInputValid()) {
            Person person = new Person();
            person.setFirstName(firstName.getText().toString());
            person.setLastName(lastName.getText().toString());
            person.setEmail(email.getText().toString());
            person.setPhone(phone.getText().toString());
            person.setCompanyName(company.getText().toString());

            Log.d("UPDATE", "Person updated");

            // will call update to server
            //updatePerson(person);
        }
    }
}
