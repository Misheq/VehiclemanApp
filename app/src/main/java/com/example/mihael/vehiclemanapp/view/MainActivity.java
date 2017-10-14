package com.example.mihael.vehiclemanapp.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mihael.vehiclemanapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage);
    }

    public void startVehicleActivity(View view) {
        Intent intent = new Intent(this, VehiclesActivity.class);
        startActivity(intent);
    }

    public void startPersonActivity(View view) {
        Intent intent = new Intent(this, PersonsActivity.class);
        startActivity(intent);
    }
}