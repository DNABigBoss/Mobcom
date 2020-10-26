package com.e.resepjajanankekinian.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.DiskusiData;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 25/10/2020.
 */
public class DiskusiAdapter extends RecyclerView.Adapter<DiskusiAdapter.CustomViewHolder> {
    private final List<DiskusiData> dataList;
    private final Context context;

    public DiskusiAdapter(Context context, List<DiskusiData> dataList){
        this.context = context;
        this.dataList = dataList;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private final TextView textViewName;
        private final TextView textViewIsi;
        private final ImageView imageView;
        private final LinearLayout cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.namaDiskusi);
            textViewIsi = mView.findViewById(R.id.komenDiskusi);
            imageView = mView.findViewById(R.id.imageViewDiskusi);
            cardView = mView.findViewById(R.id.linearLayoutDiskusi);
        }
    }

    @NonNull
    @Override
    public DiskusiAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_diskusi, parent, false);
        return new DiskusiAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DiskusiAdapter.CustomViewHolder holder, int position) {
        DiskusiData diskusiData = dataList.get(position);
        holder.textViewName.setText(diskusiData.getNama());
        holder.textViewIsi.setText(diskusiData.getIsi());
//        final Integer idx = diskusiData.getId();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
