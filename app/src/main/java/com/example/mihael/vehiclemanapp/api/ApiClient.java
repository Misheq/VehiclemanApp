package com.example.mihael.vehiclemanapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.mihael.vehiclemanapp.helpers.Constants.SERVICE_URL;

/**
 * Retrofit api client for handling api calls
 * Communicates with back-end
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getApiClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVICE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
