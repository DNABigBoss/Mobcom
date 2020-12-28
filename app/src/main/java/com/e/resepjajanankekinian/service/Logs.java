package com.e.resepjajanankekinian.service;

import android.content.Context;
import android.content.Intent;

import com.e.resepjajanankekinian.BookmarkActivity;
import com.e.resepjajanankekinian.MainActivity;
import com.e.resepjajanankekinian.login;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 21/12/2020.
 */
public class Logs {
    private String id;
    private String action;
    private String type;
    private Context context;
    private Class aClass;

    public Logs(String id, String action, String type, Context context, Class aClass) {
        this.id = id;
        this.action = action;
        this.type = type;
        this.context = context;
        this.aClass = aClass;
    }

    public void createLog(String id, String action, String type, final Context context, final Class aClass){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(id, action, type);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (context != null) {
                    Intent intent = new Intent(context, aClass);
                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (context != null) {
                    Intent intent = new Intent(context, aClass);
                    context.startActivity(intent);
                }
            }
        });
    }
}
