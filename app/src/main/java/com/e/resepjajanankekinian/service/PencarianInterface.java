package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.PencarianResep;
import com.e.resepjajanankekinian.model.ResepData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PencarianInterface {

    @GET("resep")
    Call<List<ResepData>> getUsers (@Query("") String keyword);
}

