package com.itforone.gamdol.pspdf;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pspdfkit.document.DocumentSaveOptions;
import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.document.providers.DataProvider;
import com.pspdfkit.ui.PdfActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CustompdfActivity extends PdfActivity {


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void onDestroy() {

        File file_delete_init= new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/pdfFiles/").toString());
        try {
            File[] childFileList = file_delete_init.listFiles();
            Log.d("file_delete","init");
            Log.d("file_delete",file_delete_init.toString());
            Log.d("file_delete",String.valueOf(childFileList.length));
            if (file_delete_init.exists()) {
                Log.d("file_delete","exist!!");
                for (File childFile : childFileList) {
                    if (childFile.isDirectory()) {
                    } else {
                        Log.d("file_delete",childFile.getName());
                        childFile.delete(); //하위 파일
                    }
                }
                file_delete_init.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    @NonNull
    @NotNull
    @Override
    public List<Integer> onGenerateMenuItemIds(@NonNull @NotNull List<Integer> menuItems) {

        Log.d("pspdf_custom", menuItems.toString());

        if(menuItems.size()>1){

            menuItems.remove(1);
            Log.d("pspdf_custom", "contain!!!");

        }
        else{
            Log.d("pspdf_custom", "not contain!!!");
        }

        return menuItems;

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("pspdf_custom_prepare",String.valueOf(menu.size()));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("pspdf_custom_create",String.valueOf(menu.size()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onDocumentSave(@NonNull @NotNull PdfDocument document, @NonNull @NotNull DocumentSaveOptions saveOptions) {
        Log.d("pspdf_custom_save","init!");
     /*   Uri uri = document.getDocumentSource().getFileUri();
        File file = new File(uri.getPath());
        String outputFilePath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS + "/pdfFiles") +"/test_another.pdf";

        File outputFile = new File(outputFilePath);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            try {
                Files.copy(file.toPath(),outputFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            FileInputStream inStream = null;
            try {
                inStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileChannel inChannel = inStream.getChannel();
            FileChannel outChannel = outStream.getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(), outChannel);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            InputStream in = null;
            try {
                OutputStream out = new FileOutputStream(outputFile);
                try {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } finally {
                    out.close();
                }
            }catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
         Log.d("pspdf_custom_save",uri.getPath());
*/

        return super.onDocumentSave(document, saveOptions);
    }

    @Override
    public void onBackPressed() {
/*
        final PdfDocument document = requirePdfFragment().getDocument();
        if (document == null) {
            Log.d("reponse_back", "document is null!");
            return;
        }
        final DataProvider dataProvider = document.getDocumentSource().getDataProvider();

        if (dataProvider == null){
            Log.d("reponse_back", "dataprovider is null!");
            return;
        }


        final byte[] data = dataProvider.read(dataProvider.getSize(), 0);
        final RequestBody requestBody = RequestBody.create(data, MediaType.parse("application/pdf"));
        final Request request = new Request.Builder().url("http://127.0.0.1:12345").post(requestBody).build();
        final OkHttpClient client = new OkHttpClient();
        try {
            final Response response = client.newCall(request).execute();
            Log.d("reponse_back", response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

*/

     /*   Log.d("save_document", "backpress is init!!");
        final PdfDocument document = requirePdfFragment().getDocument();

        if (document == null) {
            Log.d("save_document", "document is null!!");
            return;
        }

// Manually save the document.
        try {

            Boolean flg = document.saveIfModified();
            Log.d("save_document", flg.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        super.onBackPressed();

    }
}
