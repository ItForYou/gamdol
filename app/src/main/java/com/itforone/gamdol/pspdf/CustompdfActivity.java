package com.itforone.gamdol.pspdf;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pspdfkit.document.PdfDocument;
import com.pspdfkit.ui.PdfActivity;
import java.util.List;



public class CustompdfActivity extends PdfActivity {


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);

    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d("pspdf_custom", "custom!");
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("pspdf_custom", "custom!");
        return super.onCreateOptionsMenu(menu);
    }
}
