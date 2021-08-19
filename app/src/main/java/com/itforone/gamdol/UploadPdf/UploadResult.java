package com.itforone.gamdol.UploadPdf;

import com.google.gson.annotations.SerializedName;

public class UploadResult {

    public String getResult() {
        return result;
    }

    @SerializedName("result")
    private String result;

}
