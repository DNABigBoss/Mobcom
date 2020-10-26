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
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Resep Jajanan Kekinian on 21/10/2020.
 */
public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CustomViewHolder> {
    private final List<ResepData> dataList;
    private final Context context;

    public BookmarkAdapter(Context context, List<ResepData> dataList){
        this.context = context;
        this.dataList = dataList;
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

        holder.textViewName.setText(ResepData.getNama());
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
}
