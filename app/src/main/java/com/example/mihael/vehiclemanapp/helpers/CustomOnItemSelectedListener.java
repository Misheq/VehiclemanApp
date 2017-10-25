package com.example.mihael.vehiclemanapp.helpers;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("CustomListener", "it was clicked");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
