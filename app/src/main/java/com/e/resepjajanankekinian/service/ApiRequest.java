package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.BahanData;
import com.e.resepjajanankekinian.model.DiskusiData;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.model.UserData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by Resep Jajanan Kekinian on 21/10/2020.
 */
public interface ApiRequest {
    /*
    * Get data semua resep
     */
    @GET("resep")
    Call<List<ResepData>> getAllResep();

    /*
     * Get data resep berdasarkan id, nama , bahan , limit
     */
    @GET("resep")
    Call<List<ResepData>> getResep(@Query("id") Integer id, @Query("nama") String nama, @Query("bahan") String bahan, @Query("limit") Integer limit, @Query("order") String order);

    /*
     * Get data Step resep berdasarkan id
     */
    @GET("resep")
    Call<StepResepData> getStepResep(@Query("id") Integer id);

    /*
     * Get data semua bahan, order untuk mengurutkan
     */
    @GET("bahan")
    Call<List<BahanData>> getAllBahan(@Query("order") String order);

    /*
     * Get data user
     */
    @GET("users")
    Call<List<UserData>> getUser(@Query("nama") String nama, @Query("email") String email, @Query("pass") String pass);

    /*
     * Get data bookmark
     */
    @GET("bookmark")
    Call<List<ResepData>> getBookmark(@Query("user_id") Integer user_id);

    /*
     * Get data bookmark
     */
    @GET("diskusi")
    Call<List<DiskusiData>> getDiskusi(@Query("id") Integer id, @Query("resep_id") Integer resep_id);

    /*
     * Post data user
     */
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> postUser(@Field("nama") String nama, @Field("email") String email, @Field("pass") String pass);

    /*
     * Post data Bookmark
     */
    @FormUrlEncoded
    @POST("bookmark")
    Call<ResponseBody> postBookmark(@Field("user_id") Integer user_id, @Field("resep_id") Integer resep_id);

    /*
     * Post data diskusi
     */
    @FormUrlEncoded
    @POST("diskusi")
    Call<ResponseBody> postDiskusi(@Field("isi") String isi, @Field("user_id") Integer user_id, @Field("resep_id") Integer resep_id, @Field("disukai") Integer disukai, @Field("tanggal") String tanggal);

    /*
     * Put data resep
     */
    @FormUrlEncoded
    @PUT("resep")
    Call<ResponseBody> putFavorit(@Field("id") Integer id, @Field("favorit") Integer favorit);

    /*
     * Put data resep
     */
    @FormUrlEncoded
    @PUT("resep")
    Call<ResponseBody> putResep(@Field("id") Integer id, @Field("nama") String nama, @Field("waktu_memasak") String waktu_memasak,@Field("porsi") Integer porsi, @Field("harga") Double harga, @Field("favorit") Integer favorit, @Field("dilihat") Integer dilihat, @Field("gambar") String gambar);
}
