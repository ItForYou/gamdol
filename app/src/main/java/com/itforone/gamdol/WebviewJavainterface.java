package com.itforone.gamdol;

import android.app.Activity;
import android.content.SharedPreferences;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

//import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
//import com.pspdfkit.ui.PdfActivity;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.itforone.gamdol.pspdf.CustompdfActivity;
import com.itforone.gamdol.pspdf.WebDownloadSource;
import com.pspdfkit.annotations.AnnotationType;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.configuration.sharing.ShareFeatures;
import com.pspdfkit.document.download.DownloadJob;
import com.pspdfkit.document.download.DownloadRequest;
import com.pspdfkit.document.download.Progress;
import com.pspdfkit.ui.PdfActivityIntentBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WebviewJavainterface {

    Activity activity;
    MainActivity mainActivity;
    private static final int RC_SIGN_IN = 9001;
    static final int RESULT_DOCUMENT = 9002;

    public WebviewJavainterface(Activity activity, MainActivity mainActivity) {
        this.activity = activity;
        this.mainActivity = mainActivity;
    }

    @JavascriptInterface
    public void show_viewer(String idx) {
        if (mainActivity.progressbar.getVisibility() == View.VISIBLE) {
            Toast.makeText(mainActivity.getApplicationContext(), "현재 다운로드 중인 파일이 있습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        Log.d("init_idx", idx);


        if (!idx.isEmpty()) {
            mainActivity.download_idx = idx;
        }

        WebDownloadSource webDownloadSource = null;
        try {
            webDownloadSource = new WebDownloadSource(new URL(mainActivity.getString(R.string.pdfpath) + idx + ".pdf"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        File file_saved = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") + "/" + idx + ".pdf");


        if (!file_saved.getParentFile().exists()) {
            file_saved.getParentFile().mkdir();
        }


        final DownloadRequest request = new DownloadRequest.Builder(mainActivity.getApplicationContext())
                .overwriteExisting(true)
                .outputFile(file_saved)
                .source(webDownloadSource)
                .build();

        final DownloadJob job = DownloadJob.startDownload(request);
        final PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(mainActivity.getApplicationContext())
                .editableAnnotationTypes(Arrays.asList(AnnotationType.HIGHLIGHT, AnnotationType.INK, AnnotationType.STAMP))
                .setEnabledShareFeatures(ShareFeatures.none())
                .autosaveEnabled(false)
                .disablePrinting()
                .build();

        job.setProgressListener(new DownloadJob.ProgressListenerAdapter() {
            @Override
            public void onProgress(@NonNull Progress progress) {
                //int value = (int) (100 * progress.bytesReceived / (float) progress.totalBytes);
                //  Log.d("progress_value", String.valueOf(value));
                mainActivity.progressbar.setVisibility(View.VISIBLE);
                //  mainActivity.progressbar.setProgress((int) (100 * progress.bytesReceived / (float) progress.totalBytes));
            }

            @Override
            public void onComplete(@NonNull File output) {

                mainActivity.progressbar.setVisibility(View.GONE);
                final Intent intent = PdfActivityIntentBuilder.fromUri(mainActivity, Uri.fromFile(output))
                        .configuration(config)
                        .activityClass(CustompdfActivity.class)
                        .build();
                intent.putExtra("open_idx", idx);
                // pdfActivity.showDocument(context, Uri.fromFile(output), config);
                mainActivity.startActivityForResult(intent, RESULT_DOCUMENT);
                mainActivity.flg_downloading = 0;
                mainActivity.flg_showpdf = 1;

            }

            @Override
            public void onError(@NonNull Throwable exception) {

                mainActivity.progressbar.setVisibility(View.GONE);
                Toast.makeText(mainActivity.getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @JavascriptInterface
    public void setLogininfo(String mb_id, String mb_pwd) {

        SharedPreferences pref = mainActivity.getSharedPreferences("logininfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("id",mb_id);
        editor.putString("pwd",mb_pwd);
        editor.commit();

    }

    @JavascriptInterface
    public void setlogout() {

        // Toast.makeText(activity.getApplicationContext(),"logout",Toast.LENGTH_LONG).show();
        SharedPreferences pref = activity.getSharedPreferences("logininfo", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();

    }

}


