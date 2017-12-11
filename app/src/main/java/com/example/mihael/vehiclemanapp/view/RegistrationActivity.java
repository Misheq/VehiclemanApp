package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CONNECTION_FAILED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.MANAGER_REGISTERED;

public class RegistrationActivity extends AppCompatActivity {

    private View view;
    private EditText firstName;
    private EditText lastName;
    private EditText phone;
    private EditText company;
    private EditText email;
    private EditText password;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        view = getWindow().getDecorView().getRootView();
    }

    private void setManagerFromForm() {
        InputValidator inVal = new InputValidator(this.view);

        boolean personInputOk = inVal.isPersonInputValid();
        boolean regPasswordOk = inVal.isRegistrationPasswordValid();

        if (personInputOk && regPasswordOk) {
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

        mLoadingIndicator.setVisibility(View.VISIBLE);
        final String password = manager.getPassword();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.register(manager);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 201) {
                    Toast.makeText(RegistrationActivity.this, MANAGER_REGISTERED, Toast.LENGTH_LONG).show();

                    Manager manager = response.body();
                    LoginManager.setManager(manager);

                    manager.setPassword(password);
                    startLoginActivity(manager);

                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(RegistrationActivity.this,
                                error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(RegistrationActivity.this, "Registration unsuccessful", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(RegistrationActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
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
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
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

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.SUBMIT);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            setManagerFromForm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
