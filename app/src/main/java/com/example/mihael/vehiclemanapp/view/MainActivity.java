package com.example.mihael.vehiclemanapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Manager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerAdapter recyclerAdapter;
    private List<Manager> managers;
    private Manager manager;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        /*
        Call<List<Manager>> call = apiInterface.getManagers();
        call.enqueue(new Callback<List<Manager>>() {
            @Override
            public void onResponse(Call<List<Manager>> call, Response<List<Manager>> response) {
                int statusCode = response.code();
                managers = response.body();
                recyclerAdapter = new RecyclerAdapter(managers);
                recyclerView.setAdapter(recyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<Manager>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
        */

        // returns manager by id
        Call<Manager> call = apiInterface.getManagerById(1);
        call.enqueue(new Callback<Manager>() {
            @Override
            public void onResponse(Call<Manager> call, Response<Manager> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    manager = response.body();
                    managers = new ArrayList<Manager>();
                    managers.add(manager);
                    recyclerAdapter = new RecyclerAdapter(managers);
                    recyclerView.setAdapter(recyclerAdapter);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Manager> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }
}