package com.itforone.gamdol.UploadPdf;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadService {

    @Multipart
    @POST("update_pdffile.php")
    //Call<UploadResult> uploadFile(@Part("idx") RequestBody idx);
    Call<UploadResult> uploadFile(@Part("idx") RequestBody idx, @Part MultipartBody.Part pdf_file, @Part("mb_id") RequestBody mb_id);

}
