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
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.configuration.sharing.ShareFeatures;
import com.pspdfkit.document.download.DownloadJob;
import com.pspdfkit.document.download.DownloadRequest;
import com.pspdfkit.document.download.Progress;
import com.pspdfkit.ui.PdfActivityIntentBuilder;

import java.io.File;
import java.io.IOException;
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
        public void show_viewer(int idx) {

            if(mainActivity.flg_downloading==0) {

                String outputFilePath = null;
/*
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    mainActivity.download_idx =(int) System.currentTimeMillis();
                    outputFilePath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") + "/" + mainActivity.download_idx + ".pdf";
                }
                else{
                    mainActivity.download_idx =idx;
                    outputFilePath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") + "/" + idx + ".pdf";
                }
*/
                mainActivity.download_idx =idx;
                outputFilePath = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") + "/" + idx + ".pdf";

                File outputFile = new File(outputFilePath);
                if(outputFile.getParentFile().exists()){
                    outputFile.getParentFile().mkdirs();
                }
                if (!outputFile.exists()) {

                    DownloadManager downloadManager = (DownloadManager) mainActivity.getSystemService(Context.DOWNLOAD_SERVICE);

                    Uri downloadUri = Uri.parse(mainActivity.getString(R.string.pdfpath) + idx+".pdf");
                    mainActivity.pdffileURI = Uri.fromFile(outputFile);
                    DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                    request.setTitle("다운로드 항목");
                    request.setDestinationUri(mainActivity.pdffileURI);
                    request.setAllowedOverMetered(true);


                    long mDownloadQueueId = downloadManager.enqueue(request);
                    mainActivity.flg_downloading = 1;

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                        Toast.makeText(mainActivity.getApplicationContext(), "다운이 완료되었습니다. 다시한번 클릭해주세요.", Toast.LENGTH_LONG).show();
                    }
                }
                else{

                    if(outputFile.exists() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                        try {
                            outputFile.delete();
                            DownloadManager downloadManager = (DownloadManager) mainActivity.getSystemService(Context.DOWNLOAD_SERVICE);

                            Uri downloadUri = Uri.parse(mainActivity.getString(R.string.pdfpath) + idx+".pdf");
                            mainActivity.pdffileURI = Uri.fromFile(outputFile);
                            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                            request.setTitle("다운로드 항목");
                            request.setDestinationUri(mainActivity.pdffileURI);
                            request.setAllowedOverMetered(true);
                            mainActivity.flg_downloading = 1;

                            long mDownloadQueueId = downloadManager.enqueue(request);

                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    //mainActivity.pdffileURI = Uri.fromFile(outputFile);
                    final Uri pdfuri = mainActivity.path2uri(mainActivity,Uri.fromFile(outputFile).getPath());
                    if(pdfuri!=null) {
                        final DownloadRequest request = new DownloadRequest.Builder(mainActivity)
                                .uri(pdfuri)
                                .build();


                        final DownloadJob job = DownloadJob.startDownload(request);
                        PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(mainActivity)
                                .setEnabledShareFeatures(ShareFeatures.none())
                                .disablePrinting()
                                .autosaveEnabled(false)
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
                                mainActivity.flg_showpdf =1;
                            }

                            @Override
                            public void onError(@NonNull Throwable exception) {
                                //  handleDownloadError(exception);
                            }
                        });

                    }

                }
            }
        }

    }


