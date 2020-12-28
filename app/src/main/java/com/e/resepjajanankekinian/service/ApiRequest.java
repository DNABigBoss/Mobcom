package com.e.resepjajanankekinian.service;

import com.e.resepjajanankekinian.model.BahanData;
import com.e.resepjajanankekinian.model.DiskusiData;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.model.UserData;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by Resep Jajanan Kekinian on 21/10/2020.
 */
public interface ApiRequest {
    /*
    * Get data semua resep
     */
    @Streaming
    @GET("resep")
    Call<List<ResepData>> getAllResep();

    /*
     * Get data resep berdasarkan id, nama , bahan , limit
     */
    @Streaming
    @GET("resep")
    Call<List<ResepData>> getResep(@Query("id") Integer id, @Query("nama") String nama, @Query("bahan") String bahan, @Query("limit") Integer limit, @Query("order") String order);

    /*
     * Get data Step resep berdasarkan id
     */
    @Streaming
    @GET("resep")
    Call<StepResepData> getStepResep(@Query("id") Integer id);

    /*
     * Get data semua bahan, order untuk mengurutkan
     */
    @Streaming
    @GET("bahan")
    Call<List<BahanData>> getAllBahan(@Query("order") String order);

    /*
     * Get data user
     */
    @Streaming
    @GET("users")
    Call<List<UserData>> getUser(@Query("id") Integer id, @Query("nama") String nama, @Query("email") String email, @Query("pass") String pass);

    /*
     * Get data bookmark
     */
    @Streaming
    @GET("bookmark")
    Call<List<ResepData>> getBookmark(@Query("user_id") Integer user_id);

    /*
     * Get data bookmark
     */
    @Streaming
    @GET("diskusi")
    Call<List<DiskusiData>> getDiskusi(@Query("id") Integer id, @Query("resep_id") Integer resep_id);

    /*
     * Post data user
     */
    @Streaming
    @FormUrlEncoded
    @POST("users")
    Call<ResponseBody> postUser(@Field("nama") String nama, @Field("email") String email, @Field("pass") String pass);

    /*
     * Post data Bookmark
     */
    @Streaming
    @FormUrlEncoded
    @POST("bookmark")
    Call<ResponseBody> postBookmark(@Field("user_id") Integer user_id, @Field("resep_id") Integer resep_id);

    /*
     * Post data diskusi
     */
    @Streaming
    @FormUrlEncoded
    @POST("diskusi")
    Call<ResponseBody> postDiskusi(@Field("isi") String isi, @Field("user_id") Integer user_id, @Field("resep_id") Integer resep_id, @Field("disukai") Integer disukai, @Field("tanggal") String tanggal);

    /*
     * Post data log
     */
    @Streaming
    @FormUrlEncoded
    @POST("log")
    Call<ResponseBody> postLog(@Field("user_id") String user_id,@Field("action") String action, @Field("type") String type);

    /*
     * Put data resep
     */
    @Streaming
    @FormUrlEncoded
    @PUT("resep")
    Call<ResponseBody> putView(@Field("id") Integer id, @Field("dilihat") Integer dilihat);

    /*
     * Put data Users
     */
    @Streaming
    @FormUrlEncoded
    @PUT("users")
    Call<ResponseBody> putUser(@Field("id") Integer id, @Field("nama") String nama, @Field("email") String email, @Field("pass_old") String pass_old, @Field("pass_new") String pass_new, @Field("foto") String foto);

    /*
     * Put data Users
     */
    @Streaming
    @FormUrlEncoded
    @PUT("users")
    Call<ResponseBody> putFoto(@Field("id") Integer id, @Field("pass_old") String pass_old, @Field("foto") String foto);


    /*
     * Put data resep
     */
    @Streaming
    @FormUrlEncoded
    @PUT("resep")
    Call<ResponseBody> putResep(@Field("id") Integer id, @Field("nama") String nama, @Field("waktu_memasak") String waktu_memasak,@Field("porsi") Integer porsi, @Field("harga") Double harga, @Field("favorit") Integer favorit, @Field("dilihat") Integer dilihat, @Field("gambar") String gambar);

    /*
     * Delete data Bookmark
     */
    @Streaming
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "bookmark", hasBody = true)
    Call<ResponseBody> deleteBookmark(@Field("user_id") Integer user_id, @Field("resep_id") Integer resep_id);
}
