package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.ResepData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 10/10/2020.
 */
//public interface GetService {
//    @GET("/movie")
//    Call<List<MovieData>> getAllMovie();
//}
public  interface GetService {
    @GET("resep")
    Call<List<ResepData>> getAllResep();
}
