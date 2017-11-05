package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.PersonRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for listing existing persons from database
 */

public class PersonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PersonRecyclerAdapter personRecyclerAdapter;
    private List<Person> persons;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons);

        loadPersonList();
    }

    public void loadPersonList() {

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<List<Person>> call = apiInterface.getPersons(LoginManager.getLogedInManagerToken()); // added auth token
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    persons = response.body();
                    personRecyclerAdapter = new PersonRecyclerAdapter(persons);
                    recyclerView.setAdapter(personRecyclerAdapter);
                } else {
                    Toast.makeText(PersonsActivity.this, "Loading persons failed. Status: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Toast.makeText(PersonsActivity.this, "Loading persons failed.\nPlease check internet connection", Toast.LENGTH_SHORT).show();
                Log.d("MYTAG", "something is wrong");
            }
        });
    }

    public void startAddPersonActivity(View view) {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }

    public void deletePersonById(View view) {

        Button button = view.findViewById(R.id.buttonDelete);

        int personId = Integer.parseInt((String) button.getTag());

        Call<Void> call = apiInterface.deletePerson(LoginManager.getLogedInManagerToken(), personId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if(statusCode == 204) {
                    Toast.makeText(PersonsActivity.this, "Person deleted successfully" , Toast.LENGTH_SHORT).show();
                    loadPersonList();
                } else {
                    Toast.makeText(PersonsActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("MY_TAG", "Delete went wrong");
            }
        });
    }

}
