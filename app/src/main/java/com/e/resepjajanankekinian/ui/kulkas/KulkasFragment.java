package com.e.resepjajanankekinian.ui.kulkas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.e.resepjajanankekinian.R;

public class KulkasFragment extends Fragment {

    private KulkasViewModel kulkasViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        kulkasViewModel =
                new ViewModelProvider(this).get(KulkasViewModel.class);
        View root = inflater.inflate(R.layout.fragment_kulkas, container, false);
        final TextView textView = root.findViewById(R.id.text_kulkas);
        kulkasViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}