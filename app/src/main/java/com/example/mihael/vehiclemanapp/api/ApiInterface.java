package com.example.mihael.vehiclemanapp.api;

import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Interface for accessing api endpoints
 */

public interface ApiInterface {

    // manager endpoints
    @GET("managers")
    Call<List<Manager>> getManagers();

    @GET("managers/{id}")
    Call<Manager> getManagerById(@Path("id") int id);

    // vehicle endpoints
    @GET("vehicles")
    Call<List<Vehicle>> getVehicles();

    @POST("vehicles")
    Call<PersonVehicleMapper> createVehicle(@Body PersonVehicleMapper pvm);

    // person endpoints
    @GET("persons")
    Call<List<Person>> getPersons();

    @POST("persons")
    Call<PersonVehicleMapper> createPerson(@Body PersonVehicleMapper pvm);
}
