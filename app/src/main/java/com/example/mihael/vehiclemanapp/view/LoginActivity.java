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
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void setLoginDataFromForm(View view) {

        EditText inputEmail = findViewById(R.id.inputEmail);
        EditText inputPassword = findViewById(R.id.inputPassword);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        String base = email + ":" + password;
        String auth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

        LoginManager.setLogedInManagerToken(auth);
        loginWithManager();
        //loginWithManager(auth);
    }

    private void startManageActivity() {
        Intent intent = new Intent(getApplicationContext(), ManageActivity.class);
        startActivity(intent);
    }

    private void loginWithManager() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Void> call = apiInterface.login(LoginManager.getLogedInManagerToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if(response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "Login was successful\nCode: " + response.code() , Toast.LENGTH_SHORT).show();
                    startManageActivity();

                } else {
                    Toast.makeText(LoginActivity.this, "Login failed\nCode: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login error", Toast.LENGTH_SHORT).show();
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
