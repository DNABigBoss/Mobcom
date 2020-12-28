package com.e.resepjajanankekinian.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.e.resepjajanankekinian.BookmarkActivity;
import com.e.resepjajanankekinian.MainActivity;
import com.e.resepjajanankekinian.R;
import com.e.resepjajanankekinian.maintenance;
import com.e.resepjajanankekinian.search;
import com.e.resepjajanankekinian.search_resep_bahan;
import com.e.resepjajanankekinian.service.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static final String TAG = "Main Activity";
    private RecyclerView recyclerView;
    SessionManager sessionManager;
    ProgressBar progressBar;
    private NavController navController;
    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        HashMap<String, String> user = sessionManager.getUserDetail();
        String userName = user.get(SessionManager.NAME);
        navController = Navigation.findNavController(view);

        TextView textViewPencarianSemuaBaru = root.findViewById(R.id.pencarianSemuaBaru);
        TextView textViewPencarianSemuaPopuler = root.findViewById(R.id.pencarianSemuaPopuler);
        TextView textViewUcapan = root.findViewById(R.id.textViewUcapan);
        Button button = root.findViewById(R.id.search_bar);
        Button buttonFav = root.findViewById(R.id.buttonBookmark);
        Button buttonTambahResep = root.findViewById(R.id.buttonTambahResep);
        //BottomNavigationView bottomNavigationView = root.findViewById(R.id.bottom_navigation);
        //bottomNavigationView.setSelectedItemId(R.id.home);
        progressBar = root.findViewById(R.id.progressbarmain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), search.class));
            }
        });

        buttonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), BookmarkActivity.class));
            }
        });

        buttonTambahResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(view.getContext(), maintenance.class));
            }
        });

        /* Ketika mengklik lihat semua yang terpopuler */
        textViewPencarianSemuaPopuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencarianSemua("dilihat");
            }
        });

        /* Ketika mengklik lihat semua yang terbaru */
        textViewPencarianSemuaBaru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pencarianSemua("id");
            }
        });
    }

    private void pencarianSemua(String id) {
        Intent intent = new Intent(root.getContext(), search_resep_bahan.class);
        intent.putExtra("order", id);
        startActivity(intent);
    }
}