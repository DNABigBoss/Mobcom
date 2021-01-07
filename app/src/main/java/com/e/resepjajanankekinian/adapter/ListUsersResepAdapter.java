package com.e.resepjajanankekinian.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.model.ResepUserData;
import com.e.resepjajanankekinian.resep;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.e.resepjajanankekinian.user_resep;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 07/01/2021.
 */
public class ListUsersResepAdapter extends RecyclerView.Adapter<ListUsersResepAdapter.CustomViewHolder> {
    private final Context context;
    private final List<ResepUserData> dataList;
    private final String userId;

    public ListUsersResepAdapter(Context context, List<ResepUserData> dataList, String userId) {
        this.context = context;
        this.dataList = dataList;
        this.userId = userId;
    }
    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private final TextView textViewName;
        private final ImageView imageView;
        private final CardView cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.resepNamelistresep);
            imageView = mView.findViewById(R.id.gambarlistresep);
            cardView = mView.findViewById(R.id.imagelistresep);
        }
    }

    @NonNull
    @Override
    public ListUsersResepAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_list_resep, parent, false);
        return new ListUsersResepAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListUsersResepAdapter.CustomViewHolder holder, int position) {
        ResepUserData ResepData = dataList.get(position);
        final Integer idx = ResepData.getId();
        final String nama = ResepData.getnama_resep();

        holder.textViewName.setText(nama);

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
                Intent intent = new Intent(context, user_resep.class);
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
