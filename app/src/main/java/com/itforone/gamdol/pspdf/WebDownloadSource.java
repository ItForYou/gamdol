package com.itforone.gamdol.pspdf;

import androidx.annotation.NonNull;

import com.pspdfkit.document.download.source.DownloadSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebDownloadSource implements DownloadSource {
    @NonNull
    private final URL documentURL;

    public WebDownloadSource(@NonNull URL documentURL) {
        this.documentURL = documentURL;
    }

    @Override public InputStream open() throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) documentURL.openConnection();
        connection.connect();
        return connection.getInputStream();
    }

    @Override public long getLength() {
        long length = DownloadSource.UNKNOWN_DOWNLOAD_SIZE;

        try {
            final int contentLength = documentURL.openConnection().getContentLength();
            if (contentLength != -1) {
                length = contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return length;
    }
}
