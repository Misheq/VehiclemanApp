package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.helpers.LoginManager;

/**
 * Main activity responsible for navigating between screens
 */

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
    }

    public void startVehicleActivity(View view) {
        Intent intent = new Intent(this, VehiclesActivity.class);
        startActivity(intent);
    }

    public void startPersonActivity(View view) {
        Intent intent = new Intent(this, PersonsActivity.class);
        startActivity(intent);
    }

    public void startProfileActivity(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.setLogedInManagerToken("");
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}