package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.ResepData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 14/10/2020.
 */
public interface GetSearchResep {
    @GET("resep")
    Call<List<ResepData>> getResep(@Query("id") Integer id, @Query("nama") String nama, @Query("bahan") String bahan, @Query("limit") Integer limit);
}
