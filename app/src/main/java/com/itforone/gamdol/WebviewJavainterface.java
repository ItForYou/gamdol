package com.itforone.gamdol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.webkit.JavascriptInterface;

public class WebviewJavainterface {
    Activity activity;
    MainActivity mainActivity;
    private static final int RC_SIGN_IN = 9001;
    public WebviewJavainterface(Activity activity, MainActivity mainActivity) {
        this.activity = activity;
        this.mainActivity = mainActivity;
    }


    @JavascriptInterface
    public void setlogout() {

    }


}
