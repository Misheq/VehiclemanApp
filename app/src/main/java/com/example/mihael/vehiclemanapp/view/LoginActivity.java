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

public class LoginActivity extends AppCompatActivity {

    private View view;
    private EditText inputEmail;
    private EditText inputPassword;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        view = getWindow().getDecorView().getRootView();

        Intent intent = getIntent();

        final Manager manager = (Manager) intent.getSerializableExtra("manager");

        if (manager != null) {
            setFormFromManager(manager);
        }
    }

    private void setFormFromManager(Manager manager) {
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        inputEmail.setText(manager.getEmail());
        inputPassword.setText(manager.getPassword());
    }

    private void setLoginDataFromForm() {

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);

        String email = inputEmail.getText().toString();
        String password = inputPassword.getText().toString();

        InputValidator inVal = new InputValidator(this.view);

        if (inVal.isLoginValid()) {
            String base = email + ":" + password;
            String auth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);

            LoginManager.setLogedInManagerToken(auth);
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

        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.login(LoginManager.getLogedInManagerToken(), email);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                if (response.code() == 200) {
                    Manager manager = response.body();
                    LoginManager.setManager(manager);
                    startManageActivity();

                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(LoginActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(LoginActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Overrides back button on phone to prevent from getting back into the app
     * without authorization
     */
    @Override
    public void onBackPressed() {
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.SIGN_IN);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            setLoginDataFromForm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
