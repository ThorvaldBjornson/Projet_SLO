package com.example.slo_domotic;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

public interface API {

    @GET("data")
    Call<Data> getDataEnvironnement();
    @GET("led")
    Call<Led> getLedColor();


    @PUT("led")
    Call<String> putLedColor(@Body RequestBody ledValue);
}
