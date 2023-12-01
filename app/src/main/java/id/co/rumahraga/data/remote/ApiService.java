package id.co.rumahraga.data.remote;

import id.co.rumahraga.model.EventModel;
import id.co.rumahraga.model.NotificationModel;
import id.co.rumahraga.model.TransactionDetailModel;
import id.co.rumahraga.model.TransactionModel;
import id.co.rumahraga.model.BannerModel;
import id.co.rumahraga.model.BookedModel;
import id.co.rumahraga.model.CategoryModel;
import id.co.rumahraga.model.CityModel;
import id.co.rumahraga.model.FieldModel;
import id.co.rumahraga.model.JamModel;
import id.co.rumahraga.model.PaymentMethodModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.ReviewModel;
import id.co.rumahraga.model.UserModel;

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
    public static final String END_POINT = "https://rumahraga.com/rumah_raga/api/";
    public static final String BASE_URL = "https://rumahraga.com/rumah_raga/";

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

    @GET("user/get_detail_trans")
    Call<ResponseModel<List<TransactionDetailModel>>> getDetailTransaction(
            @Query("id") String transactionCode);

    @FormUrlEncoded
    @POST("user/insert_review")
    Call<ResponseModel> insertReview(
            @Field("transaction_id") int transactionId,
            @Field("user_id") String userId,
            @Field("review_text") String reviewText,
            @Field("field_id") int fieldId,
            @Field("stars") float stars
    );

    @GET("user/get_reviews")
    Call<ResponseModel<List<ReviewModel>>> getReview(
            @Query("id") int fieldId
    );

    @GET("user/get_total_review")
    Call<ResponseModel<ReviewModel>> getTotalReview(
            @Query("id") int fieldId
    );

    @GET("user/get_notification")
    Call<ResponseModel<List<NotificationModel>>> getNotification(
            @Query("user_id") String userId,
            @Query("is_read") int isRead
    );

    @FormUrlEncoded
    @POST("user/update_notification")
    Call<ResponseModel> updateNotification(
            @Field("user_id") String userId
    );

    @GET("user/get_user")
    Call<ResponseModel<UserModel>> getUser(
            @Query("user_id") String userId);

    @FormUrlEncoded
    @POST("user/update_username")
    Call<ResponseModel> updateUsername(
            @Field("username") String username,
            @Field("user_id") String userId
    );

    @FormUrlEncoded
    @POST("user/update_password")
    Call<ResponseModel> updatePassword(
            @Field("user_id") String userId,
            @Field("old_password") String oldPassword,
            @Field("new_password") String newPassword
    );

    @Multipart
    @POST("user/update_profile_photo")
    Call<ResponseModel> updateProfilePhoto(
            @PartMap Map<String, RequestBody> textData,
            @Part MultipartBody.Part filePart
    );

    @FormUrlEncoded
    @POST("user/delete_notification")
    Call<ResponseModel> deleteNotification(
            @Field("id") String id
    );

    @GET("user/get_event")
    Call<ResponseModel<List<EventModel>>> getEvents();

}
