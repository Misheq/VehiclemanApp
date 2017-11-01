package com.example.mihael.vehiclemanapp.helpers;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.mihael.vehiclemanapp.R;
import com.example.mihael.vehiclemanapp.api.ApiClient;
import com.example.mihael.vehiclemanapp.api.ApiInterface;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import com.example.mihael.vehiclemanapp.interfaces.SpinnerEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mihae on 2017. 11. 01..
 */

public class SpinnerLoader {

    ApiInterface apiInterface;
    List<Person> persons = new ArrayList<>();
    List<Vehicle> vehicles = new ArrayList<>();
    Spinner personsSpinner;
    Spinner vehiclesSpinner;
    View view;
    ArrayAdapter<Person> personArrayAdapter;

    private SpinnerEventListener spinnerEventListenerVehicle;

    public SpinnerLoader(View view) {
        this.view = view;
    }

    public void setEventListener(SpinnerEventListener spinnerEventListenerVehicle) {
        this.spinnerEventListenerVehicle = spinnerEventListenerVehicle;
    }

    public void loadPersonsSpinnerForVehicle() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns persons list
        Call<List<Person>> call = apiInterface.getPersons();
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("DEBUG", "Get persons successful");
                    persons = response.body();
                    addPersonOnSpinner(persons);

                    if (spinnerEventListenerVehicle != null) {
                        spinnerEventListenerVehicle.onEventAccured();
                    }

                }else {
                    Log.d("DEBUG", "Get persons not successful, status: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<Person>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }

    private void addPersonOnSpinner(List<Person> persons) {
        personsSpinner = view.findViewById(R.id.spinnerPersons);

        Person dummyPerson = new Person();
        dummyPerson.setFirstName("Select");
        dummyPerson.setLastName("person");
        persons.add(0, dummyPerson);

        personArrayAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, persons);
        personArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personsSpinner.setAdapter(personArrayAdapter);
    }

    public ArrayAdapter<Person> getPersonArrayAdapter() {
        return personArrayAdapter;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public Spinner getPersonsSpinner() {
        return personsSpinner;
    }

    public Spinner getVehiclesSpinner() {
        return vehiclesSpinner;
    }

    //loadVehiclesSpinnerForPerson() {
    //
    //}

}
