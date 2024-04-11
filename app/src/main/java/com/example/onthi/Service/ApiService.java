package com.example.onthi.Service;

import com.example.onthi.modal.Response;
import com.example.onthi.modal.Xemay;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    String DO_MAIN = "http://192.168.100.3:3000/api/";

    ApiService apiservice =new Retrofit.Builder()
            .baseUrl(ApiService.DO_MAIN)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService.class);

    @GET("list")
    Call<Response<List<Xemay>>> getData();

    @GET("xemaybyid/{id}")
    Call<Response<Xemay>> getXe(@Path("id") String id);

    @Multipart
    @POST("add")
    Call<Response<Xemay>> addXe(@Part("ten_xe_ph42693") RequestBody ten_xe_ph42693,
                                @Part("mau_sac_ph42693") RequestBody mau_sac_ph42693,
                                @Part("gia_ban_ph42693") RequestBody gia_ban_ph42693,
                                @Part("mo_ta_ph42693") RequestBody mo_ta_ph42693,
                                @Part MultipartBody.Part hinh_anh_ph42693);

    @DELETE("delete/{id}")
    Call<Response<Xemay>> deleteXe(@Path("id") String id);

    @Multipart
    @PUT("update/{id}")
    Call<Response<Xemay>> updateXe(@Path("id") String id,
                                   @Part("ten_xe_ph42693") RequestBody ten_xe_ph42693,
                                   @Part("mau_sac_ph42693") RequestBody mau_sac_ph42693,
                                   @Part("gia_ban_ph42693") RequestBody gia_ban_ph42693,
                                   @Part("mo_ta_ph42693") RequestBody mo_ta_ph42693,
                                   @Part MultipartBody.Part image);

    @GET("search")
    Call<Response<List<Xemay>>> searchXe(@Query("key") String key);

    @GET("sort")
    Call<Response<List<Xemay>>> sortXe(@Query("type") int type);
}
