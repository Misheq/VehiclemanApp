package com.example.mihael.vehiclemanapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit api client for handling api calls
 */

public class ApiClient {

    public static final String BASE_URL = "http://192.168.0.104:8000/vehicleman/api/"; // 192.168.0.104
    public static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

}
