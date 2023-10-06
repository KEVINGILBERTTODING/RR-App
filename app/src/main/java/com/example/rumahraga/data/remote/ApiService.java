package com.example.rumahraga.data.remote;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    public static final String END_POINT = "http://192.168.100.6/rumah_raga/api/";

    @FormUrlEncoded
    @POST("login")

}
