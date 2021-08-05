package com.itforone.gamdol;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.itforone.gamdol.pspdf.CustompdfActivity;
import com.pspdfkit.configuration.activity.PdfActivityConfiguration;
import com.pspdfkit.configuration.sharing.ShareFeatures;
import com.pspdfkit.document.download.DownloadJob;
import com.pspdfkit.document.download.DownloadRequest;
import com.pspdfkit.document.download.Progress;
import com.pspdfkit.ui.PdfActivityIntentBuilder;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    static final int PERMISSION_REQUEST_CODE = 1;
    final int FILECHOOSER_LOLLIPOP_REQ_CODE = 1300;
    ValueCallback<Uri[]> filePathCallbackLollipop;
    Uri mCapturedImageURI,pdffileURI;
    @BindView(R.id.mWebview)
    WebView webView;
    private long backPrssedTime = 0;
    int flg_downloading = 0, flg_showpdf=0, download_idx;
    CustompdfActivity pdfActivity = new CustompdfActivity();


    public void set_filePathCallbackLollipop(ValueCallback<Uri[]> filePathCallbackLollipop) {
        this.filePathCallbackLollipop = filePathCallbackLollipop;
    }

    public static Uri path2uri(Context context, String filePath) {

        Cursor cursor = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d("path2uri", "Q");
            cursor = context.getContentResolver().query(MediaStore.Downloads.EXTERNAL_CONTENT_URI, null, "_data = '" + filePath + "'", null, null);
        }
        else
        {
            Log.d("path2uri", "else Q");
            cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"),null,"_data = '" + filePath + "'",null,null);
        }

        cursor.moveToNext();

        Uri uri = null;
        Log.d("path2uri", filePath);
        if(cursor!=null && cursor.moveToFirst()) {

            Log.d("path2uri", "firstexist");
            int id = cursor.getInt(cursor.getColumnIndex("_id"));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uri = ContentUris.withAppendedId(MediaStore.Downloads.EXTERNAL_CONTENT_URI, id);
            }
            else{
                uri = ContentUris.withAppendedId(MediaStore.Files.getContentUri("external"), id);
            }

            //    Toast.makeText(context.getApplicationContext(), "version is low!!", Toast.LENGTH_LONG).show();

        }


        return uri;
    }


    private BroadcastReceiver downdloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            flg_downloading = 0;
            File file_saved = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") +"/"+String.valueOf(download_idx)+".pdf");


            if (file_saved.exists() && pdffileURI != null) {

                final Uri pdfuri = path2uri(context,pdffileURI.getPath());
                if(pdfuri!=null) {
                    final DownloadRequest request = new DownloadRequest.Builder(MainActivity.this)
                            .uri(pdfuri)
                            .build();


                    final DownloadJob job = DownloadJob.startDownload(request);
                    PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(MainActivity.this)
                            .setEnabledShareFeatures(ShareFeatures.none())
                            .disablePrinting()
                            .build();



                    job.setProgressListener(new DownloadJob.ProgressListenerAdapter() {
                        @Override
                        public void onProgress(@NonNull Progress progress) {
                            // progressBar.setProgress((int) (100 * progress.bytesReceived / (float) progress.totalBytes));
                        }

                        @Override
                        public void onComplete(@NonNull File output) {

                            final Intent intent = PdfActivityIntentBuilder.fromUri(context, Uri.fromFile(output))
                                    .configuration(config)
                                    .activityClass(CustompdfActivity.class)
                                    .build();
                            // pdfActivity.showDocument(context, Uri.fromFile(output), config);
                            startActivity(intent);
                            flg_showpdf =1;
                        }

                        @Override
                        public void onError(@NonNull Throwable exception) {
                            //  handleDownloadError(exception);
                        }
                    });

                }





//                final Uri pdfuri = Uri.parse("file:///android_asset/temp_pdf.pdf");

                //final Uri pdfuri = Uri.parse("file:///Download/temppdf/temp_pdf.pdf");
                //Log.d("pspdf_file", String.valueOf(pdfuri.getPath()));
//                PdfActivityConfiguration config = new PdfActivityConfiguration.Builder(MainActivity.this)
//                        .setEnabledShareFeatures(ShareFeatures.none())
//                        .disablePrinting()
//                        .autosaveEnabled(false)
//                        .build();
//
//                CustompdfActivity.showDocument(MainActivity.this, pdfuri, null, config);

            } else {

                Toast.makeText(getApplicationContext(), "not exist!!!", Toast.LENGTH_LONG).show();

            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);


        Intent intent = getIntent();
        if (intent != null && Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();

            if (uri != null) {
                Log.d("Tag", uri.toString());
            }

        }

        registerReceiver(downdloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        webView.setWebChromeClient(new ChromeManager(this, this));
        webView.setWebViewClient(new ViewManager(this, this));
        webView.addJavascriptInterface(new WebviewJavainterface(this, this), "Android");

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setSupportMultipleWindows(true);
        settings.setAllowFileAccess(true);//웹에서 파일 접근 여부
        settings.setAppCacheEnabled(true);//캐쉬 사용여부
        settings.setDatabaseEnabled(true);//HTML5에서 db 사용여부 -> indexDB
        settings.setDomStorageEnabled(true);//HTML5에서 DOM 사용여부
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);//캐시 사용모드 LOAD_NO_CACHE는 캐시를 사용않는다는 뜻
        settings.setTextZoom(100);       // 폰트크기 고정
        settings.setUserAgentString(settings.getUserAgentString() + "gamdolapp");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setWebContentsDebuggingEnabled(true);
        }

        webView.loadUrl(getString(R.string.home));

    }

    @Override
    public void onBackPressed() {

        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPrssedTime;

            if (!webView.canGoBack() || webView.getUrl().contains("all_contact")) {
                if (0 <= intervalTime && 2000 >= intervalTime) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAndRemoveTask();
                    } else {
                        finish();
                    }

                } else {
                    backPrssedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "한번 더 뒤로가기 누를시 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                webView.goBack();
            }

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case FILECHOOSER_LOLLIPOP_REQ_CODE: {
                Uri[] result = new Uri[0];
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // 이미지 다중선택 추가
                    if (data != null && data.getClipData() != null) {
                        int cnt = data.getClipData().getItemCount();

                        result = new Uri[cnt];
                        for (int i = 0; i < cnt; i++) {
                            result[i] = data.getClipData().getItemAt(i).getUri();

                        }
                    } else {
                        if (data == null)
                            data = new Intent();
                        if (data.getData() == null)
                            data.setData(mCapturedImageURI);
                        //Toast.makeText(this,mCapturedImageURI.toString()+"2", Toast.LENGTH_LONG).show();
                        filePathCallbackLollipop.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, data));
                        filePathCallbackLollipop = null;
                        return;
                    }

                    filePathCallbackLollipop.onReceiveValue(result);
                    filePathCallbackLollipop = null;
                }
                break;
            }

        }
    }
}