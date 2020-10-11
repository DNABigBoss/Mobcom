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
import com.squareup.picasso.Target;

import java.util.List;


/**
 * Created by Dwiki Sulthon Saputra Marbi on 10/10/2020.
 */
public class Adapter extends RecyclerView.Adapter<Adapter.CustomViewHolder> {
    private List<ResepData> dataList;
    private Context context;

    public Adapter(Context context, List<ResepData> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private TextView textViewName;
        private ImageView imageView;
        private CardView cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.textViewName);
            imageView = mView.findViewById(R.id.imageView);
            cardView = mView.findViewById(R.id.cardView);
        }
    }

    @NonNull
    @Override
    public Adapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Adapter.CustomViewHolder holder, int position) {
        ResepData ResepData = dataList.get(position);
        holder.textViewName.setText(ResepData.getNama());
        final Integer idx = (Integer) ResepData.getId();
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(ResepData.getGambar())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LayoutInflater.from(context).getContext(), resep.class);
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
