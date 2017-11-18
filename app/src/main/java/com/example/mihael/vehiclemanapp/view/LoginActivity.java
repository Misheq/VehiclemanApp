package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private View view;
    private EditText inputEmail;
    private EditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        view = getWindow().getDecorView().getRootView();

        Intent intent = getIntent();

        final Manager manager = (Manager) intent.getSerializableExtra("manager");

        if(manager != null) {
            setFormFromManager(manager);
        }
    }

    private void setFormFromManager(Manager manager) {
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        inputEmail.setText(manager.getEmail());
        inputPassword.setText(manager.getPassword());
    }

    public void setLoginDataFromForm(View view) {

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isLoginValid()) {
            String base = email + ":" + password;
            String auth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

            LoginManager.setLogedInManagerToken(auth);
            // TODO: save hash to user preferences, user can stay loged in if it is not empty, and empty shared preferences when user clicks on logout in manage screen
            loginWithManager(email);
        }
    }

    public void startRegisterActivity(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    private void startManageActivity() {
        Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
        startActivity(intent);
    }

    private void loginWithManager(String email) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.login(LoginManager.getLogedInManagerToken(), email);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {

                if(response.code() == 200) {
                    Manager manager = response.body();
                    // save or pass to activity
                    LoginManager.setManager(manager);
                    startManageActivity();

                } else if(response.code() >=300 && response.code() < 500) {
                    Toast.makeText(LoginActivity.this, "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Server error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Overrides back button on phone to prevent from getting back into the app
     * without authorization
     */
    @Override
    public void onBackPressed() { }
}
