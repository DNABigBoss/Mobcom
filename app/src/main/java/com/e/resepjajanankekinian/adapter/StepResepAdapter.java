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
import com.e.resepjajanankekinian.model.StepResepData;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 11/10/2020.
 */
public class StepResepAdapter extends RecyclerView.Adapter<StepResepAdapter.CustomViewHolder> {
    private List<StepResepData.DatumBahan> dataBahan;
    private List<StepResepData.DatumInfo> dataInfo;
    private List<StepResepData.DatumStep> datastep;
    private Context context;

    public StepResepAdapter(Context context, List<StepResepData.DatumInfo> dataInfo){
        this.context = context;
        this.dataInfo = dataInfo;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private TextView namajajanan;
        private ImageView imagejajanan;
        private TextView dilihat;
        private TextView favorit;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            namajajanan = mView.findViewById(R.id.namajajanan);
            imagejajanan = mView.findViewById(R.id.imagejajanan);
            dilihat = mView.findViewById(R.id.dilihat);
            favorit = mView.findViewById(R.id.favorit);

        }
    }

    @NonNull
    @Override
    public StepResepAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_resep, parent, false);
        return new StepResepAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (position == 0) {
            StepResepData.DatumInfo datumInfo = dataInfo.get(position);
            holder.namajajanan.setText(datumInfo.getNama());
            holder.dilihat.setText(datumInfo.getDilihat());
            holder.favorit.setText(datumInfo.getFavorit());
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(datumInfo.getGambar())
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imagejajanan);
        }

    }


    @Override
    public int getItemCount() {
        return dataInfo.size();
    }
}
