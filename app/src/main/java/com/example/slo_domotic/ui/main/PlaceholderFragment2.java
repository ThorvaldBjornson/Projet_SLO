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
import com.example.slo_domotic.Led;
import com.example.slo_domotic.R;
import com.example.slo_domotic.databinding.Fragment2MainBinding;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorChangedListener;
import com.flask.colorpicker.OnColorSelectedListener;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment2 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private Fragment2MainBinding binding;

    public static PlaceholderFragment2 newInstance(int index) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
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

        binding = Fragment2MainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ColorPickerView colorPickerView = binding.colorPickerView;
        colorPickerView.addOnColorChangedListener(new OnColorChangedListener() {
            @Override public void onColorChanged(int selectedColor) {
                TextView label = binding.Label;
                label.setText("0x" + Integer.toHexString(selectedColor));
                TextView labelRGB = binding.LabelRGB;
                int color = selectedColor;
                float red   = (color >> 16) & 0xFF;
                float green = (color >> 8)  & 0xFF;
                float blue  = (color)       & 0xFF;
                float alpha = (color >> 24) & 0xFF;
                putLedValue(selectedColor);
                labelRGB.setText("R :" + String.valueOf(red) + " G :" + String.valueOf(green) + " B :" + String.valueOf(blue));
            }
        });
        colorPickerView.addOnColorSelectedListener(new OnColorSelectedListener() {
            @Override
            public void onColorSelected(int selectedColor) {
                TextView label = binding.Label;
                label.setText("0x" + Integer.toHexString(selectedColor));
                TextView labelRGB = binding.LabelRGB;
                int color = selectedColor;
                float red   = (color >> 16) & 0xFF;
                float green = (color >> 8)  & 0xFF;
                float blue  = (color)       & 0xFF;
                putLedValue(selectedColor);
                labelRGB.setText("R :" + String.valueOf(red) + " G :" + String.valueOf(green) + " B :" + String.valueOf(blue));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void putLedValue(int Color){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+ getString(R.string.BROKER_IP) +"/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create((API.class));
        Led led = new Led();
        led.setR((Color >> 16) & 0xFF);
        led.setG((Color >> 8)  & 0xFF);
        led.setB((Color)       & 0xFF);
        led.setValue("rgb("+led.getR()+","+led.getG()+","+led.getB()+")");
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("text/html; charset=utf-8"),led.getValue());
        Call<String> call = api.putLedColor(body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(!response.isSuccessful()){

                    //Toast.makeText(getContext().getApplicationContext(),"No Response " + response.code() + response.message(),Toast.LENGTH_LONG).show();

                    return;
                }
                else{
                    //Toast.makeText(getContext().getApplicationContext(),"Updated Led: "+response.body(),Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //Toast.makeText(getContext().getApplicationContext(),"Failure" + t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }
}