package com.e.resepjajanankekinian.adapter;

import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.StepResepData;
import com.e.resepjajanankekinian.service.ApiClient;
import com.e.resepjajanankekinian.service.ApiRequest;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 11/10/2020.
 */
public class StepResepAdapter extends RecyclerView.Adapter<StepResepAdapter.CustomViewHolder> {
    private Context context;
    private List<StepResepData.DatumStep> datumStep;
    private String userId;

    public StepResepAdapter(Context context, List<StepResepData.DatumStep> datumStep, String userId) {
        this.context = context;
        this.datumStep = datumStep;
        this.userId = userId;
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder{

        public final View mView;

        private TextView nomorstepresep;
        private TextView stepresep;
        //private Button buttonLanjut;
        private ToggleButton buttonSound;
        private TextToSpeech t1;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            nomorstepresep = mView.findViewById(R.id.nomorstepresep);
            stepresep = mView.findViewById(R.id.stepresep);
            //buttonLanjut = mView.findViewById(R.id.lanjut);
            buttonSound = mView.findViewById(R.id.buttonSound);

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
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final StepResepData.DatumStep dataStep = datumStep.get(position);
        holder.nomorstepresep.setText(String.valueOf(dataStep.getNomor_step()));
        holder.stepresep.setText(dataStep.getIntruksi());
        Integer count = getItemCount();
        //if (position == count-1) holder.buttonLanjut.setVisibility(View.GONE);
        holder.mView.setTag(position);
        final Locale locale = new java.util.Locale("id","ID","id-ID");
        holder.t1 = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    holder.t1.setLanguage(locale);
                }
            }
        });

        holder.buttonSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLog("menekan tombol suara", "click");
                if (holder.buttonSound.isChecked()) {
                    holder.buttonSound.setChecked(true);
                    holder.t1.speak(dataStep.getIntruksi(),TextToSpeech.QUEUE_FLUSH, null, "");
                } else {
                    holder.buttonSound.setChecked(false);
                    holder.t1.stop();
                }
            }
        });
    }




    @Override
    public int getItemCount() {
        return datumStep.size();
    }

    private void createLog(String action, String type){
        final ApiRequest apiRequest = ApiClient.getRetrofitInstance().create(ApiRequest.class);
        Call<ResponseBody> responseBodyCall = apiRequest.postLog(userId, action, type);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }
}
