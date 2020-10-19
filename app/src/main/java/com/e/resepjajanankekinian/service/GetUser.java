package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Resep Jajanan Kekinian on 17/10/2020.
 */
public interface GetUser {
    @GET("users")
    Call<List<UserData>> getUser(@Query("nama") String nama,@Query("email") String email, @Query("pass") String pass);
}
