package com.e.resepjajanankekinian.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.resep;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Resep Jajanan Kekinian on 21/10/2020.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CustomViewHolder> {
    private final Context context;
    private final List<ResepData> dataList;
    private final String userId;

    public BookmarkAdapter(Context context, List<ResepData> dataList, String userId) {
        this.context = context;
        this.dataList = dataList;
        this.userId = userId;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private final TextView textViewName;
        private final TextView textViewDilihat;
        private final TextView textViewFavorit;
        private final ImageView imageView;
        private final CardView cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.resepNameBookmark);
            imageView = mView.findViewById(R.id.gambarBookmark);
            cardView = mView.findViewById(R.id.imageBookmark);
            textViewDilihat = mView.findViewById(R.id.dilihatbookmark);
            textViewFavorit = mView.findViewById(R.id.favoritbookmark);
        }
    }

    @NonNull
    @Override
    public BookmarkAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_bookmark, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookmarkAdapter.CustomViewHolder holder, int position) {
        ResepData ResepData = dataList.get(position);
        final Integer idx = ResepData.getId();
        final String nama = ResepData.getNama();

        holder.textViewName.setText(nama);
        holder.textViewDilihat.setText(String.valueOf(ResepData.getDilihat()));
        holder.textViewFavorit.setText(String.valueOf(ResepData.getFavorit()));

        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(ResepData.getGambar())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> responseBodyCall = createLog("melihat resep "+nama, "watch");
                responseBodyCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
                Intent intent = new Intent(context, resep.class);
                intent.putExtra("id", idx);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private Call<ResponseBody> createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userId, action, type);
        return responseBodyCall;
    }
}
