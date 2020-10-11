package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.StepResepData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public  interface GetResep {
    @GET("/resep/")
    Call<StepResepData> getResep(@Query("id") Integer id);
}
