package com.example.mihael.vehiclemanapp.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        loadVehicleList();
    }

    /**
     * starts the add vehicle activity
     * @param view
     */
    public void startAddVehicleActivity(View view) {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    /**
     * api call which gets all the vehicles which the given manager
     * has access to.
     */
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

    /**
     * opens dialog alert, prevents deletion by accident
     * @param view
     */
    public void alertForDelete(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Delete confirmation");

        // set dialog message
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this element?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        Button button = view.findViewById(R.id.buttonDelete);
                        final int vehicleId = Integer.parseInt((String) button.getTag());

                        deleteVehicleById(vehicleId);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * api call which deletes the given vehicle
     * @param vehicleId
     */
    private void deleteVehicleById(int vehicleId) {

        Call<Void> call = apiInterface.deleteVehicle(LoginManager.getLogedInManagerToken(), vehicleId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if(statusCode == 204) {
                    Toast.makeText(VehiclesActivity.this, "Vehicle deleted successfully.", Toast.LENGTH_SHORT).show();
                    loadVehicleList();
                } else {
                    Toast.makeText(VehiclesActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VehiclesActivity.this, "Delete went wrong", Toast.LENGTH_LONG).show();
                Log.d("MY_TAG", "Delete went wrong");
            }
        });
    }
}