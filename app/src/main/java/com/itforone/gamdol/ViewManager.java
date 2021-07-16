package com.itforone.gamdol;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class ViewManager extends WebViewClient {

    Activity context;
    MainActivity mainActivity;

    public ViewManager(Activity activity, MainActivity mainActivity) {
        this.mainActivity  = mainActivity;
        this.context = activity;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        //로그인, 글쓰기, 회원가입, 정보수정 뒤로가기 처리
        //Toast.makeText(mainActivity.getApplicationContext(),url, Toast.LENGTH_LONG).show();

        view.loadUrl(url);
        return false;

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        // mainActivity.dialogloading.show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }


}
