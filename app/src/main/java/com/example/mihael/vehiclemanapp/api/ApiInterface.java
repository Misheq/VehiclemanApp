package com.example.mihael.vehiclemanapp.api;

import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.PersonVehicleMapper;
import com.example.mihael.vehiclemanapp.entities.Vehicle;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.example.mihael.vehiclemanapp.helpers.Constants.ID;
import static com.example.mihael.vehiclemanapp.helpers.Constants.ID_PARAM;
import static com.example.mihael.vehiclemanapp.helpers.Constants.PERSONS;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SEPARATOR;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLES;

/**
 * Interface for accessing api endpoints
 * Contains all api call declarations
 */

public interface ApiInterface {

    // manager endpoints

    /*
    @GET("managers")
    Call<List<Manager>> getManagers();

    @GET("managers/{id}")
    Call<Manager> getManagerById(@Path("id") int id);
    */

    ///////// vehicle endpoints //////////
    @GET(VEHICLES)
    Call<List<Vehicle>> getVehicles();

    @POST(VEHICLES)
    Call<PersonVehicleMapper> createVehicle(@Body PersonVehicleMapper pvm);

    @PUT(VEHICLES + SEPARATOR + ID_PARAM)
    Call<Vehicle> updateVehicle(@Path(ID) int id, @Body Vehicle vehicle);

    @DELETE(VEHICLES + SEPARATOR + ID_PARAM)
    Call<Void> deleteVehicle(@Path(ID) int id);

    ////////// person endpoints //////////
    @GET(PERSONS)
    Call<List<Person>> getPersons();

    @POST(PERSONS)
    Call<PersonVehicleMapper> createPerson(@Body PersonVehicleMapper pvm);

    @PUT(PERSONS + SEPARATOR + ID_PARAM)
    Call<Person> updatePerson(@Path(ID) int id, @Body Person person);

    @DELETE(PERSONS + SEPARATOR + ID_PARAM)
    Call<Void> deletePerson(@Path(ID) int id);
}
