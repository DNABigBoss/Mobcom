package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.BahanData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 12/10/2020.
 */
public interface GetBahan {
    @GET("bahan")
    Call<List<BahanData>> getAllBahan(@Query("order") String order);
}
