package com.example.slo_domotic.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slo_domotic.API;
import com.example.slo_domotic.Data;
import com.example.slo_domotic.R;
import com.example.slo_domotic.databinding.FragmentMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    public FragmentMainBinding binding;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView tempLabel = binding.TempLabel;
        tempLabel.setText("Température : ");
        final TextView humLabel = binding.HumLabel;
        humLabel.setText("humidité ambiante : 55%");
        final TextView qualLabel = binding.QualLabel;
        qualLabel.setText("Qualité de l'air : 26111");
        final TextView lumLabel = binding.LumLabel;
        lumLabel.setText("Luminosité: 7500");

        FloatingActionButton fab = binding.fab;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDataEnvironnement();
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void LoadDataEnvironnement(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ getString(R.string.BROKER_IP) +"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create((API.class));
        Call<Data> call = api.getDataEnvironnement();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(!response.isSuccessful()){

                    return;
                }
                else{
                    Data data = response.body();
                    binding.TempLabel.setText("Température : " + data.getTemp());
                    binding.HumLabel.setText("Humidité ambiante : " + data.getHum());
                    binding.QualLabel.setText("Qualité de l'air : " + data.getQual());
                    binding.LumLabel.setText("Luminosité : " + data.getLum());
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
            }
        });
    }
}