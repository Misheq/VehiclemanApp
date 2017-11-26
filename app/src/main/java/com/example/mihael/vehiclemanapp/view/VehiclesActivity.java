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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.adapters.VehicleRecyclerAdapter;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.helpers.Constants;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mihael.vehiclemanapp.helpers.Constants.CONNECTION_FAILED;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DELETE_VEHICLE_SUCCESSFUL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_MESSAGE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_NO;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_TITLE;
import static com.example.mihael.vehiclemanapp.helpers.Constants.DIALOG_BOX_YES;

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
    private ProgressBar mLoadingIndicator;
    private TextView mNoVehiclesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);

        loadVehicleList();
    }

    /**
     * starts the add vehicle activity
     */
    private void startAddVehicleActivity() {
        Intent intent = new Intent(this, AddVehicleActivity.class);
        startActivity(intent);
    }

    /**
     * api call which gets all the vehicles which the given manager
     * has access to.
     */
    private void loadVehicleList() {

        mNoVehiclesTextView = findViewById(R.id.tv_no_vehicles);
        mLoadingIndicator = findViewById(R.id.pb_loading_indicator);
        mLoadingIndicator.setVisibility(View.VISIBLE);

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
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                if (statusCode == 200) {
                    vehicles = response.body();

                    if(vehicles.size() == 0) {
                        displayNoVehicleMessage();
                    } else {
                        hideNoVehicleMessage();
                    }


                    vehicleRecyclerAdapter = new VehicleRecyclerAdapter(vehicles);
                    recyclerView.setAdapter(vehicleRecyclerAdapter);
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(VehiclesActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                Toast.makeText(VehiclesActivity.this, CONNECTION_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * opens dialog alert, prevents deletion by accident
     */
    public void alertForDelete(final View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle(DIALOG_BOX_TITLE);

        // set dialog message
        alertDialogBuilder
                .setMessage(DIALOG_BOX_MESSAGE)
                .setCancelable(false)
                .setPositiveButton(DIALOG_BOX_YES, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ImageButton button = view.findViewById(R.id.buttonDelete);
                        final int vehicleId = Integer.parseInt((String) button.getTag());

                        deleteVehicleById(vehicleId);
                    }
                })
                .setNegativeButton(DIALOG_BOX_NO, new DialogInterface.OnClickListener() {
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
     */
    private void deleteVehicleById(int vehicleId) {

        Call<Void> call = apiInterface.deleteVehicle(LoginManager.getLogedInManagerToken(), vehicleId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                int statusCode = response.code();
                if (statusCode == 204) {
                    Toast.makeText(VehiclesActivity.this, DELETE_VEHICLE_SUCCESSFUL, Toast.LENGTH_SHORT).show();
                    loadVehicleList();
                } else {
                    try {
                        JSONObject error = new JSONObject(response.errorBody().string());
                        Toast.makeText(VehiclesActivity.this,
                                "Status code: " + statusCode + "\n"
                                        + error.getString("error"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(VehiclesActivity.this, CONNECTION_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    // helpers

    private void displayNoVehicleMessage() {
        mNoVehiclesTextView.setVisibility(View.VISIBLE);
    }

    private void hideNoVehicleMessage() {
        mNoVehiclesTextView.setVisibility(View.INVISIBLE);
    }

    // MENU HELPER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_item);
        item.setTitle(Constants.NEW);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.action_item) {
            startAddVehicleActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}