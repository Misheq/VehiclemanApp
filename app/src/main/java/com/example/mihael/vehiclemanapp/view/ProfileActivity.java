package com.example.mihael.vehiclemanapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import static com.example.mihael.vehiclemanapp.helpers.Constants.MANAGER_UPDATED;

public class ProfileActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText company;
    private View view;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        view = getWindow().getDecorView().getRootView();
        getManager(LoginManager.getManagerId());
    }

    private void getManager(int managerId) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.getManagerById(LoginManager.getLogedInManagerToken(), managerId);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 200) {
                    Manager manager = response.body();
                    LoginManager.setManager(manager);
                    setFieldsFromManager(manager);

                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ProfileActivity.this, "Status code: " + statusCode + "\n" + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(ProfileActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setFieldsFromManager(Manager manager) {
        getUiElements();
        setUiTextsFromManager(manager);
    }

    private void setManagerFromForm() {
        InputValidator inVal = new InputValidator(this.view);

        if (inVal.isPersonInputValid()) {
            Manager manager = createManagerFromUiTexts();
            manager.setManagerId(LoginManager.getManager().getManagerId());

            editManager(manager);
        }
    }

    private void editManager(Manager manager) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.updateManager(LoginManager.getLogedInManagerToken(), manager.getManagerId(), manager);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 200) {
                    Toast.makeText(ProfileActivity.this, MANAGER_UPDATED, Toast.LENGTH_LONG).show();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ProfileActivity.this,
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
                Toast.makeText(ProfileActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    /////// helpers ////////
    private void getUiElements() {
        firstName = findViewById(R.id.inputFirstName);
        lastName = findViewById(R.id.inputLastName);
        email = findViewById(R.id.inputEmail);
        phone = findViewById(R.id.inputPhone);
        company = findViewById(R.id.inputCompany);
    }

    private void setUiTextsFromManager(Manager manager) {
        firstName.setText(manager.getFirstName());
        lastName.setText(manager.getLastName());
        email.setText(manager.getEmail());
        phone.setText(manager.getPhone());
        company.setText(manager.getCompanyName());
    }

    private Manager createManagerFromUiTexts() {
        Manager manager = new Manager();

        manager.setFirstName(firstName.getText().toString());
        manager.setLastName(lastName.getText().toString());
        manager.setEmail(email.getText().toString());
        manager.setPhone(phone.getText().toString());
        manager.setCompanyName(company.getText().toString());

        return manager;
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.SAVE);

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