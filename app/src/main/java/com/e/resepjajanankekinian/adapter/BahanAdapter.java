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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.model.BahanData;
import com.e.resepjajanankekinian.search_resep_bahan;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Dwiki Sulthon Saputra Marbi on 12/10/2020.
 */
public class BahanAdapter extends RecyclerView.Adapter<BahanAdapter.CustomViewHolder> implements Filterable {
    private final List<BahanData> dataList;
    private final List<BahanData> dataListCopy;
    private final Context context;

    public BahanAdapter(Context context, List<BahanData> dataList){
        this.context = context;
        this.dataList = dataList;
        dataListCopy = new ArrayList<>(dataList);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<BahanData> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0) {
                filteredList.addAll(dataListCopy);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (BahanData bahanData : dataListCopy) {
                    // abdi, ali, budi if (nama == isian) abdi,ali
                    if (bahanData.getNama().toLowerCase().contains(filterPattern)) {
                        filteredList.add(bahanData);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataList.clear();
            dataList.addAll((Collection<? extends BahanData>) results.values);
            notifyDataSetChanged();
        }
    };

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        private final ImageView imageView;
        private final TextView textViewName;
        private final CardView cardView;

        CustomViewHolder(View itemView){
            super(itemView);
            mView = itemView;
            textViewName = mView.findViewById(R.id.textViewNamaBahan);
            imageView = mView.findViewById(R.id.imageViewBahan);
            cardView = mView.findViewById(R.id.cardViewBahan);
        }
    }

    @NonNull
    @Override
    public BahanAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_row_kulkas, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BahanAdapter.CustomViewHolder holder, int position) {
        BahanData bahanData = dataList.get(position);
        final String nama = bahanData.getNama();
        holder.textViewName.setText(nama);
        Picasso.Builder builder = new Picasso.Builder(context);
        builder.downloader(new OkHttp3Downloader(context));
        builder.build().load(bahanData.getGambar())
                .placeholder((R.drawable.ic_launcher_background))
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, search_resep_bahan.class);
                intent.putExtra("bahan", nama);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
