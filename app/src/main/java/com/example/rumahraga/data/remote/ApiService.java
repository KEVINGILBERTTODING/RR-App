package com.example.rumahraga.data.remote;

import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    public static final String END_POINT = "http://192.168.100.6/rumah_raga/api/";

    @FormUrlEncoded
    @POST("auth/login")
    Call<ResponseModel<UserModel>> login(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("auth/register")
    Call<ResponseModel<UserModel>> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("name") String name,
            @Field("password") String password
    );



}