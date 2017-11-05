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

import static com.example.mihael.vehiclemanapp.helpers.Constants.PERSON;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SELECT;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLE;

/**
 * Spinner loader is a helper class to load the spinner object and populate with data
 * from the database
 * It also filters the objects to show only available vehicles and unassigned persons
 */

public class SpinnerLoader {

    private ApiInterface apiInterface;
    private List<Person> persons = new ArrayList<>();
    private List<Vehicle> vehicles = new ArrayList<>();
    private Spinner personsSpinner;
    private Spinner vehiclesSpinner;
    private View view;
    private int currentId;

    private SpinnerEventListener spinnerEventListener;

    public SpinnerLoader(View view, int currentId) {
        this.view = view;
        this.currentId = currentId;
    }

    public void setEventListener(SpinnerEventListener eventListener) {
        this.spinnerEventListener = eventListener;
    }

    public void loadPersonsSpinnerForVehicle() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns persons list
        Call<List<Person>> call = apiInterface.getPersonsForManager(LoginManager.getLogedInManagerToken(), LoginManager.getManagerId()); // added auth
        call.enqueue(new Callback<List<Person>>() {
            @Override
            public void onResponse(Call<List<Person>> call, Response<List<Person>> response) {
                int statusCode = response.code();

                if(statusCode == 200) {
                    Log.d("DEBUG", "Get persons successful");
                    persons = response.body();
                    addPersonsToSpinner(persons);

                    if (spinnerEventListener != null) {
                        spinnerEventListener.onEventOccured();
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

    private void addPersonsToSpinner(List<Person> persons) {
        personsSpinner = view.findViewById(R.id.spinnerPersons);

        Person dummyPerson = new Person();
        dummyPerson.setFirstName(SELECT);
        dummyPerson.setLastName(PERSON);

        persons = setAvailablePersons(persons);
        persons.add(0, dummyPerson);

        this.persons = persons;

        ArrayAdapter<Person> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, persons);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        personsSpinner.setAdapter(adapter);
    }

    public void loadVehiclesSpinnerForPerson() {

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // returns vehicle list
        // should only return vehicles with no person and current person

        Call<List<Vehicle>> call = apiInterface.getVehiclesForManager(LoginManager.getLogedInManagerToken(), LoginManager.getManagerId());
        call.enqueue(new Callback<List<Vehicle>>() {
            @Override
            public void onResponse(Call<List<Vehicle>> call, Response<List<Vehicle>> response) {
                int statusCode = response.code();
                if(statusCode == 200) {
                    Log.d("DEBUG", "Get vehicles successful");
                    vehicles = response.body();
                    addVehiclesToSpinner(vehicles);

                    if (spinnerEventListener != null) {
                        spinnerEventListener.onEventOccured();
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

    public void addVehiclesToSpinner(List<Vehicle> vehicles) {
        vehiclesSpinner = view.findViewById(R.id.spinnerVehicles);

        Vehicle dummyVehicle = new Vehicle();
        dummyVehicle.setVehicleType(SELECT);
        dummyVehicle.setRegistrationNumber(VEHICLE);

        vehicles = setAvailableVehicles(vehicles);
        vehicles.add(0, dummyVehicle);

        this.vehicles = vehicles;

        ArrayAdapter<Vehicle> adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, vehicles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vehiclesSpinner.setAdapter(adapter);
    }

    private List<Person> setAvailablePersons(List<Person> personList) {
        // all persons whose vehicle list is empty and who is assigned to the given vehicle

        List<Person> availablePersons = new ArrayList<>();

        for(int i = 0; i < personList.size(); i++) {
            if(personList.get(i).getVehicles().size() == 0 || personList.get(i).getPersonId() == currentId) {
                availablePersons.add(personList.get(i));
            }
        }

        return availablePersons;
    }

    private List<Vehicle> setAvailableVehicles(List<Vehicle> vehicleList) {
        // all vehicles whose assigneeId is empty and the currently assigned vehicle
        List<Vehicle> availableVehicles = new ArrayList<>();

        for(int i = 0; i < vehicleList.size(); i++) {
            if(vehicleList.get(i).getAssigneeId().equals("") || vehicleList.get(i).getVehicleId() == currentId) {
                availableVehicles.add(vehicleList.get(i));
            }
        }

        return availableVehicles;
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
