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

    private ApiInterface apiInterface;
    private List<Person> persons = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private Spinner personsSpinner;
    private Spinner vehiclesSpinner;
    private View view;

    private SpinnerEventListener spinnerEventListenerVehicle;
    private SpinnerEventListener spinnerEventListenerPerson;

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

        ArrayAdapter<Person> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, persons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personsSpinner.setAdapter(adapter);
    }

    public void loadVehicleSpinnerForPerson() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns vehicle list
        // should only return vehicles with no person and current person

        Call<List<Vehicle>> call = apiInterface.getVehicles();
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    Log.d("DEBUG", "Get vehicles successful");
                    vehicles = response.body();
                    addVehicleOnSpinner(vehicles);

                    if (spinnerEventListenerPerson != null) {
                        spinnerEventListenerPerson.onEventAccured();
                    }
                }

                Log.d("DEBUG", "Get vehicles not successful, status: " + statusCode);
            }

            @Override
            public void onFailure(Call<List<Vehicle>> call, Throwable t) {
                Log.d("MYTAG","something is wrong");
            }
        });
    }

    public void addVehicleOnSpinner(List<Vehicle> vehicles) {
        vehiclesSpinner = view.findViewById(R.id.spinnerVehicles);

        Vehicle dummyVehicle = new Vehicle();
        dummyVehicle.setVehicleType("Select");
        dummyVehicle.setRegistrationNumber("vehicle");
        vehicles.add(0, dummyVehicle);

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiclesSpinner.setAdapter(adapter);
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
}
