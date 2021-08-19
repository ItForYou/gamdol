package com.itforone.gamdol.UploadPdf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadHelper {

    private static final String baseurl = "http://itforone.com/~gamdol/bbs/";

    public static Retrofit getRetrofit() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //클라이언트가 서버를 연결할 때 필요한 객체
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(httpLoggingInterceptor)//json파싱을 문서 파싱 된 것을 console에 띄우기 위함
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

    }
}