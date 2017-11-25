package com.example.mihael.vehiclemanapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class ProfileActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText phone;
    private EditText company;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        view = getWindow().getDecorView().getRootView();
        getManager(LoginManager.getManagerId());
    }

    private void getManager(int managerId) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.getManagerById(LoginManager.getLogedInManagerToken(), managerId);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nManager updated successfully!");
                    Manager manager = response.body();
                    LoginManager.setManager(manager);
                    setFieldsFromManager(manager);

                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nGet manager failed");
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
                Toast.makeText(ProfileActivity.this, "Something went wrong whit get manager by id", Toast.LENGTH_LONG).show();
                Log.d("MY_ERROR", "something is wrong with get manager");
            }
        });
    }

    private void setFieldsFromManager(Manager manager) {
        // TODO: change password, nice to have ( add a field for old password, and 2 for the new, if everything is ok, then send, if empty, then it should not update password)

        getUiElements();
        setUiTextsFromManager(manager);
    }

    private void setManagerFromForm() {
        getUiElements();

        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isPersonInputValid()) {
            Manager manager = createManagerFromUiTexts();

            // how to handle password?
            manager.setManagerId(LoginManager.getManager().getManagerId());
            //manager.setPassword(LoginManager.getManager().getPassword());

            // will call update to server
            editManager(manager);
        }
    }

    private void editManager(Manager manager) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Manager> call = apiInterface.updateManager(LoginManager.getLogedInManagerToken(), manager.getManagerId(), manager);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nManager updated successfully!");
                    Toast.makeText(ProfileActivity.this, "Manager updated", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nManager not updated!");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ProfileActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        // what if exception occures?
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                Log.d("MY_ERROR", "something is wrong with person update");
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