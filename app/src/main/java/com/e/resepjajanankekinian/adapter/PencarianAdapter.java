package com.e.resepjajanankekinian.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.ResepData;
import com.e.resepjajanankekinian.resep;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PencarianAdapter extends RecyclerView.Adapter<PencarianAdapter.MyViewHolder> implements Filterable {

    private final List<ResepData> reseps;
    private final List<ResepData> resepscopy;
    private final Context context;
    private final String userId;

    public PencarianAdapter(List<ResepData> reseps, Context context, String userId){
        this.reseps = reseps;
        this.context = context;
        this.userId = userId;
        resepscopy = new ArrayList<>(reseps);
    }

    @Override
    public Filter getFilter(){
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ResepData> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(resepscopy); // nambahin semua data asli yang udh dicopy di userscopy kalo gaada ketikan
            }else{
                String input = constraint.toString().toLowerCase().trim();
                for(ResepData resep : resepscopy){
                    if(resep.getNama().toLowerCase().contains(input)){
                        filteredList.add(resep);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            reseps.clear();
            reseps.addAll((Collection<? extends ResepData>) results.values);
            notifyDataSetChanged();
        }
    };

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nama;
        ImageView gambar;
        CardView cardView;

        public MyViewHolder(View itemView){
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            gambar = itemView.findViewById(R.id.gambar);
            cardView = itemView.findViewById(R.id.itemcardview);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itempencarian, parent, false); // masukin ke item.xml
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.nama.setText(reseps.get(position).getNama());
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(reseps.get(position).getGambar())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.gambar);
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, resep.class);
                intent.putExtra("id", reseps.get(position).getId());
                context.startActivity(
                        intent
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return reseps.size();
    }

}
