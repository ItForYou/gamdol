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
import android.webkit.JavascriptInterface;
import android.widget.Toast;

//import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
//import com.pspdfkit.ui.PdfActivity;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.itforone.gamdol.pspdf.CustompdfActivity;
import com.itforone.gamdol.pspdf.WebDownloadSource;
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
import java.util.List;

public class WebviewJavainterface {

    Activity activity;
    MainActivity mainActivity;
    private static final int RC_SIGN_IN = 9001;

    public WebviewJavainterface(Activity activity, MainActivity mainActivity) {
        this.activity = activity;
        this.mainActivity = mainActivity;
    }

    @JavascriptInterface
    public void show_viewer(String idx) {

        WebDownloadSource webDownloadSource = null;
        try {
             webDownloadSource = new WebDownloadSource(new URL(mainActivity.getString(R.string.pdfpath)+idx+".pdf"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        File file_saved = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") +"/"+idx+".pdf");

        if(!file_saved.getParentFile().exists()){
            file_saved.getParentFile().mkdir();
        }

        final DownloadRequest request = new DownloadRequest.Builder(mainActivity.getApplicationContext())
                .overwriteExisting(true)
                .outputFile(file_saved)
                .source(webDownloadSource)
                .build();

        final DownloadJob job = DownloadJob.startDownload(request);
        PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(mainActivity.getApplicationContext())
                .setEnabledShareFeatures(ShareFeatures.none())
                .autosaveEnabled(false)
                .disablePrinting()
                .build();

        job.setProgressListener(new DownloadJob.ProgressListenerAdapter() {
            @Override
            public void onProgress(@NonNull Progress progress) {
                // progressBar.setProgress((int) (100 * progress.bytesReceived / (float) progress.totalBytes));
            }

            @Override
            public void onComplete(@NonNull File output) {

                final Intent intent = PdfActivityIntentBuilder.fromUri(mainActivity, Uri.fromFile(output))
                        .configuration(config)
                        .activityClass(CustompdfActivity.class)
                        .build();
                // pdfActivity.showDocument(context, Uri.fromFile(output), config);
                mainActivity.startActivity(intent);
                mainActivity.flg_downloading = 0;
                mainActivity.flg_showpdf =1;
            }

            @Override
            public void onError(@NonNull Throwable exception) {
                //  handleDownloadError(exception);
            }
        });


    }

}


