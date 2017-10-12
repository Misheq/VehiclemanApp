package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.PersonRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private PersonRecyclerAdapter personRecyclerAdapter;
    private List<Person> persons;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<List<Person>> call = apiInterface.getPersons();
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();
                persons = response.body();
                personRecyclerAdapter = new PersonRecyclerAdapter(persons);
                recyclerView.setAdapter(personRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }
}
