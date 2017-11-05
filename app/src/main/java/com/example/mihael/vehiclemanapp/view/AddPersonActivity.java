package com.example.mihael.vehiclemanapp.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.InputValidator;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;
import com.example.mihael.vehiclemanapp.helpers.SpinnerLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT_VEHICLE;

/**
 * Activity which is responsible for creating persons
 */

public class AddPersonActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private View view;
    private SpinnerLoader spinnerLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        view = getWindow().getDecorView().getRootView();
        spinnerLoader = new SpinnerLoader(view, -1);
        spinnerLoader.loadVehiclesSpinnerForPerson();
    }

    public void setPersonFromForm(View view) {

        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText email = findViewById(R.id.inputEmail);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);
        Spinner vehiclesSpinner = findViewById(R.id.spinnerVehicles);


        Object selectedVehicle = vehiclesSpinner.getSelectedItem();
        List<Vehicle> vehicleList = new ArrayList<>();

        Person person = new Person();

        if(!selectedVehicle.toString().equals(SELECT_VEHICLE)) {
            Vehicle v = (Vehicle) selectedVehicle;
            vehicleList.add(v);
            person.setVehicles(vehicleList);
        }

        InputValidator inVal = new InputValidator(this.view);

        if(inVal.isPersonInputValid()) {
            person.setFirstName(firstName.getText().toString());
            person.setLastName(lastName.getText().toString());
            person.setEmail(email.getText().toString());
            person.setPhone(phone.getText().toString());
            person.setCompanyName(company.getText().toString());
            person.setManagerId(LoginManager.getManagerId());

            Log.d("ADD", "Person created");

            savePerson(person);
        }
    }

    private void savePerson(Person person) {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Person> call = apiInterface.createPerson(LoginManager.getLogedInManagerToken(), person);
        call.enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                int statusCode = response.code();

                if(statusCode == 201) {
                    Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson created successfully!");
                    Toast.makeText(AddPersonActivity.this, "Person created", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Log.d("STATUS_CODE", "status:" + statusCode + "\nPerson not created!");
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(AddPersonActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong with person create");
            }
        });
    }
}
