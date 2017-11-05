package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adaptors.VehicleRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity responsible for listing existing vehicles from database
 */

public class VehiclesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private VehicleRecyclerAdapter vehicleRecyclerAdapter;
    private List<Vehicle> vehicles;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        loadVehicleList();
    }

    public void startAddVehicleActivity(View view) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    public void loadVehicleList() {

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns vehicle list
        Call<List<Vehicle>> call = apiInterface.getVehiclesForManager(LoginManager.getLogedInManagerToken(), LoginManager.getManagerId());
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    vehicles = response.body();
                    vehicleRecyclerAdapter = new VehicleRecyclerAdapter(vehicles);
                    recyclerView.setAdapter(vehicleRecyclerAdapter);
                } else {
                    Toast.makeText(VehiclesActivity.this, "Loading vehicles failed. Status: " + statusCode, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Toast.makeText(VehiclesActivity.this, "Loading vehicles failed.\nPlease check internet connection." , Toast.LENGTH_SHORT).show();
                Log.d("MYTAG","something is wrong");
            }
        });
    }

    public void deleteVehicleById(View view) {
        Button button = view.findViewById(R.id.buttonDelete);

        final int vehicleId = Integer.parseInt((String) button.getTag());

        Call<Void> call = apiInterface.deleteVehicle(LoginManager.getLogedInManagerToken(), vehicleId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if(statusCode == 204) {
                    Toast.makeText(VehiclesActivity.this, "Vehicle deleted successfully. Id: " + vehicleId , Toast.LENGTH_SHORT).show();
                    loadVehicleList();
                } else {
                    Toast.makeText(VehiclesActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VehiclesActivity.this, "Delete failed on failure", Toast.LENGTH_SHORT).show();
                Log.d("MY_TAG", "Delete went wrong");
            }
        });
    }
}

 /*
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns manager list
        Call<List<Manager>> call = apiInterface.getManagers();
        call.enqueue(new Callback<List<Manager>>() {
            @Override
            public void onResponse(Call<List<Manager>> call, Response<List<Manager>> response) {
                int statusCode = response.code();
                managers = response.body();
                managerRecyclerAdapter = new ManagerRecyclerAdapter(managers);
                recyclerView.setAdapter(managerRecyclerAdapter);
            }

            @Override
            public void onFailure(Call<List<Manager>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });


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
                    managerRecyclerAdapter = new ManagerRecyclerAdapter(managers);
                    recyclerView.setAdapter(managerRecyclerAdapter);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(ManageActivity.this,
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
    */
