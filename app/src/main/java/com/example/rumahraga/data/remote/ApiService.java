package com.example.rumahraga.data.remote;

import com.example.rumahraga.model.TransactionModel;
import com.example.rumahraga.model.BannerModel;
import com.example.rumahraga.model.BookedModel;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.JamModel;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.model.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiService {
    public static final String END_POINT = "http://192.168.100.6/rumah_raga/api/";
    public static final String BASE_URL = "http://192.168.100.6/rumah_raga/";

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

    @GET("user/get_all_category")
    Call<ResponseModel<List<CategoryModel>>> getAllCategory();

    @GET("user/field_closer")
    Call<ResponseModel<List<FieldModel>>> getFieldCloser(
            @Query("city") String city
    );

    @GET("user/get_banner")
    Call<ResponseModel<List<BannerModel>>> getAllBanner();

    @GET("user/get_field_by_id")
    Call<ResponseModel<FieldModel>> getFieldById(
            @Query("id") String id
    );

    @GET("user/get_total_rating")
    Call<ResponseModel<ReviewModel>> getTotalRating(
            @Query("id") String id
    );

    @GET("user/get_jam")
    Call<ResponseModel<List<JamModel>>> getHour(
            @Query("field_id") String fieldId,
            @Query("date") String date
    );

    @GET("user/get_field_categories")
    Call<ResponseModel<List<FieldModel>>> getFieldByCategory(
            @Query("id") String id
    );

    @GET("user/get_all_city")
    Call<ResponseModel<List<CityModel>>> getAllCity();

    @GET("user/get_field_filter")
    Call<ResponseModel<List<FieldModel>>> filterField(
            @Query("city_name") String cityName,
            @Query("category_name") String categoryName
     );

    @GET("user/get_field_all")
    Call<ResponseModel<List<FieldModel>>> getAllField();

    @FormUrlEncoded
    @POST("user/update_location")
    Call<ResponseModel> updateLocation(
            @Field("user_id") String userId,
            @Field("city_name") String cityName
    );

    @GET("user/get_payment")
    Call<ResponseModel<List<PaymentMethodModel>>> getAllPayment();

    @GET("user/get_payment_detail")
    Call<ResponseModel<PaymentMethodModel>> getPaymentDetail(
            @Query("id") String id
    );

    @Multipart
    @POST("user/insert_transaction")
    Call<ResponseModel> insertTransaction(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part filePart
            );


    @POST("user/insert_detail_transaction")
    Call<ResponseModel> insertDetailTransaction(
            @Body List<BookedModel> bookedModel
    );

    @GET("user/get_transactions")
    Call<ResponseModel<List<TransactionModel>>> getMyTransaction(
            @Query("id") String id
    );



}
