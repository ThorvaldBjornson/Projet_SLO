package com.example.slo_domotic;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Led {

    @SerializedName("R")
    private String R;

    @SerializedName("G")
    private String G;

    @SerializedName("B")
    private String B;

    @SerializedName("Value")
    @Expose
    private String value;

    public String getR() {
        return R;
    }

    public void setR(float r) {
        R = String.valueOf((int)r);
    }

    public String getG() {
        return G;
    }

    public void setG(float g) {
        G = String.valueOf((int)g);
    }

    public String getB() {
        return B;
    }

    public void setB(float b) {
        B = String.valueOf((int)b);
    }

    public void setValue(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }
}
