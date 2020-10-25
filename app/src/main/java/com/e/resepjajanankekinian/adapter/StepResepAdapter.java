package com.e.resepjajanankekinian.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private List<StepResepData.DatumStep> datumStep;
    private Context context;

    public StepResepAdapter(Context context, List<StepResepData.DatumStep> datumStep){
        this.context = context;
        this.datumStep = datumStep;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private TextView nomorstepresep;
        private TextView stepresep;
        private Button buttonLanjut;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            nomorstepresep = mView.findViewById(R.id.nomorstepresep);
            stepresep = mView.findViewById(R.id.stepresep);
            buttonLanjut = mView.findViewById(R.id.lanjut);
        }
    }

    @NonNull
    @Override
    public StepResepAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row_step, parent, false);
        return new StepResepAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        StepResepData.DatumStep dataStep = datumStep.get(position);
        holder.nomorstepresep.setText(String.valueOf(dataStep.getNomor_step()));
        holder.stepresep.setText(dataStep.getIntruksi());
        Integer count = getItemCount();
        if (position != count-1) {
            
        }
    }


    @Override
    public int getItemCount() {
        return datumStep.size();
    }
}
