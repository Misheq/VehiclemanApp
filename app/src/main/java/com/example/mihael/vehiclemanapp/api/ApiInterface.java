package com.example.mihael.vehiclemanapp.api;

import com.example.mihael.vehiclemanapp.entities.Manager;
import com.example.mihael.vehiclemanapp.entities.Person;
import com.example.mihael.vehiclemanapp.entities.Vehicle;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import static com.example.mihael.vehiclemanapp.helpers.Constants.AUTH;
import static com.example.mihael.vehiclemanapp.helpers.Constants.AUTHORIZATION;
import static com.example.mihael.vehiclemanapp.helpers.Constants.EMAIL;
import static com.example.mihael.vehiclemanapp.helpers.Constants.EMAIL_PARAM;
import static com.example.mihael.vehiclemanapp.helpers.Constants.ID;
import static com.example.mihael.vehiclemanapp.helpers.Constants.ID_PARAM;
import static com.example.mihael.vehiclemanapp.helpers.Constants.LOGIN;
import static com.example.mihael.vehiclemanapp.helpers.Constants.MANAGERS;
import static com.example.mihael.vehiclemanapp.helpers.Constants.PERSONS;
import static com.example.mihael.vehiclemanapp.helpers.Constants.REGISTER;
import static com.example.mihael.vehiclemanapp.helpers.Constants.SEPARATOR;
import static com.example.mihael.vehiclemanapp.helpers.Constants.VEHICLES;

/**
 * Interface for accessing api endpoints
 * Contains all api call declarations
 */

public interface ApiInterface {

    //////////////////////////////////////
    ////////// manager endpoints /////////
    //////////////////////////////////////

    @GET(MANAGERS + SEPARATOR + ID_PARAM + SEPARATOR + PERSONS)
    Call<List<Person>> getPersonsForManager(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id);

    @GET(MANAGERS + SEPARATOR + ID_PARAM + SEPARATOR + VEHICLES)
    Call<List<Vehicle>> getVehiclesForManager(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id);

    @GET(MANAGERS + SEPARATOR + ID_PARAM)
    Call<Manager> getManagerById(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id);

    @PUT(MANAGERS + SEPARATOR + ID_PARAM)
    Call<Manager> updateManager(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id, @Body Manager manager);

    //////////////////////////////////////
    ///////// vehicle endpoints //////////
    //////////////////////////////////////

    @POST(VEHICLES)
    Call<Vehicle> createVehicle(@Header(AUTHORIZATION) String authHeader, @Body Vehicle vehicle);

    @PUT(VEHICLES + SEPARATOR + ID_PARAM)
    Call<Vehicle> updateVehicle(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id, @Body Vehicle vehicle);

    @DELETE(VEHICLES + SEPARATOR + ID_PARAM)
    Call<Void> deleteVehicle(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id);

    //////////////////////////////////////
    ////////// person endpoints //////////
    //////////////////////////////////////

    @POST(PERSONS)
    Call<Person> createPerson(@Header(AUTHORIZATION) String authHeader, @Body Person person);

    @PUT(PERSONS + SEPARATOR + ID_PARAM)
    Call<Person> updatePerson(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id, @Body Person person);

    @DELETE(PERSONS + SEPARATOR + ID_PARAM)
    Call<Void> deletePerson(@Header(AUTHORIZATION) String authHeader, @Path(ID) int id);

    ////////////////////////////////////////
    //////////// authorization /////////////
    ////////////////////////////////////////
    @GET(AUTH + SEPARATOR + LOGIN + SEPARATOR + EMAIL_PARAM)
    Call<Manager> login(@Header(AUTHORIZATION) String authHeader, @Path(EMAIL) String email);

    @POST(AUTH + SEPARATOR + REGISTER)
    Call<Manager> register(@Body Manager manager);
}