package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private View view;
    private EditText firstName;
    private EditText lastName;
    private EditText phone;
    private EditText company;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        view = getWindow().getDecorView().getRootView();
    }

    public void setManagerFromForm(View view) {
        InputValidator inVal = new InputValidator(this.view);

        // manager and person input is identical (for the required fields)
        if(inVal.isPersonInputValid() && inVal.isRegistrationPasswordValid()) {
            Manager manager = createManagerFromUi();

            saveManagerToke();

            // will call update to server
            registerManager(manager);
        }
    }

    private void startLoginActivity(Manager manager) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.putExtra("manager", manager);
        startActivity(intent);
    }

    private void registerManager(Manager manager) {

        final String password = manager.getPassword();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.register(manager);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();

                if(statusCode == 201) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nManager registered");
                    Toast.makeText(RegistrationActivity.this, "Manager registered", Toast.LENGTH_LONG).show();

                    Manager manager = response.body();
                    LoginManager.setManager(manager);

                    // set password back to plain text to be able to login since i dont go to manage activity from this screen
                    // workaround!!
                    manager.setPassword(password);
                    startLoginActivity(manager);

                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nManager registration failed");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(RegistrationActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.d("MY_ERROR", "something is wrong with manager registration");
            }
        });

    }

    //////////// helpers /////////////

    private void getUiElements() {
        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);
        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
    }

    private Manager createManagerFromUi() {
        getUiElements();

        Manager manager = new Manager();

        manager.setFirstName(firstName.getText().toString());
        manager.setLastName(lastName.getText().toString());
        manager.setPhone(phone.getText().toString());
        manager.setCompanyName(company.getText().toString());
        manager.setEmail(email.getText().toString());
        manager.setPassword(password.getText().toString());

        return manager;
    }

    private void saveManagerToke() {
        String base = email.getText().toString() + ":" + password.getText().toString();
        String auth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        LoginManager.setLogedInManagerToken(auth);
    }
}
