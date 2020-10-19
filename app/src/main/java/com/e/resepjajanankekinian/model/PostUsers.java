package com.e.resepjajanankekinian.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Resep Jajanan Kekinian on 17/10/2020.
 */
public interface PostUsers {
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> postUser(@Field("nama") String nama, @Field("email") String email, @Field("pass") String pass);
}
