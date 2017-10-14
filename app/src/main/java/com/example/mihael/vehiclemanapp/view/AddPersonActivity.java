package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.PersonRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPersonActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
    }

    private void savePerson(Person person) {

        PersonVehicleMapper pvm = new PersonVehicleMapper(person, new ArrayList<Vehicle>());

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<PersonVehicleMapper> call = apiInterface.createPerson(pvm);
        call.enqueue(new Callback<PersonVehicleMapper>() {
            @Override
            public void onResponse(Call<PersonVehicleMapper> call, Response<PersonVehicleMapper> response) {
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
            public void onFailure(Call<PersonVehicleMapper> call, Throwable t) {
                Log.d("MY_ERROR", "something is wrong");
            }
        });
    }

    public void setPersonFromForm(View view) {

        EditText firstName = findViewById(R.id.inputFirstName);
        EditText lastName = findViewById(R.id.inputLastName);
        EditText email = findViewById(R.id.inputEmail);
        EditText phone = findViewById(R.id.inputPhone);
        EditText company = findViewById(R.id.inputCompany);

        Person person = new Person();
        person.setFirstName(firstName.getText().toString());
        person.setLastName(lastName.getText().toString());
        person.setEmail(email.getText().toString());
        person.setPhone(phone.getText().toString());
        person.setCompanyName(company.getText().toString());

        savePerson(person);
    }
}
