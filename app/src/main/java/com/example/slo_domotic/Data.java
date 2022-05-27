package com.example.slo_domotic;

import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("tmp")
    private String Temp;

    @SerializedName("hum")
    private String Hum;

    @SerializedName("air")
    private String Qual;

    @SerializedName("foto")
    private String Lum;

    public String getTemp(){ return Temp; }

    public String getHum(){ return Hum; }

    public String getQual(){ return Qual; }

    public String getLum(){ return Lum; }
}
