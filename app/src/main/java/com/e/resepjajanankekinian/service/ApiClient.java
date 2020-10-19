package com.e.resepjajanankekinian.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 10/10/2020.
 */
public class ApiClient {
    public static Retrofit retrofit;
    public static final String BASE_URL = "https://apimarbidev.000webhostapp.com/";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
