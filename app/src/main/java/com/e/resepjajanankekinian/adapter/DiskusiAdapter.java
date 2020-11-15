package com.e.resepjajanankekinian.adapter;

import android.annotation.SuppressLint;
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
import com.e.resepjajanankekinian.profil;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        private final TextView textViewTanggal;
        private final ImageView imageView;
        private final LinearLayout cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.namaDiskusi);
            textViewIsi = mView.findViewById(R.id.komenDiskusi);
            imageView = mView.findViewById(R.id.imageViewDiskusi);
            cardView = mView.findViewById(R.id.linearLayoutDiskusi);
            textViewTanggal = mView.findViewById(R.id.tanggalDiskusi);
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
        String tanggal = setTanggal(diskusiData.getTanggal());
        holder.textViewTanggal.setText(tanggal);
        String foto = "https://resepjajanankekinian.my.id/assets/img/users/"+diskusiData.foto;
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(foto)
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
//        final Integer idx = diskusiData.getId();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    private String setTanggal(String tanggalKomen) {
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tanggalSaatIni = dateFormat.format(Calendar.getInstance().getTime());
        try {
            Date d1 = dateFormat.parse(tanggalKomen);
            Date d2 = dateFormat.parse(tanggalSaatIni);

            long diff = d2.getTime() - d1.getTime();
            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diffDays >= 1) return String.format("%s hari",diffDays);
            else if(diffHours >= 1) return String.format("%s jam",diffHours);
            else if(diffMinutes >= 1) return String.format("%s menit",diffMinutes);
            else if(diffSeconds >= 1) return String.format("%s detik",diffSeconds);
        } catch (ParseException e) {
            e.printStackTrace();
            return "null";
        }
        return "null";
    }
}
