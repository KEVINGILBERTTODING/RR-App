package id.co.rumahraga.data.repository.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.ReviewModel;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {

    private ApiService apiService;

    @Inject
    public ReviewRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<ReviewModel>> getTotalRating(String id) {
        MutableLiveData<ResponseModel<ReviewModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getTotalRating(id).enqueue(new Callback<ResponseModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ReviewModel>> call, Response<ResponseModel<ReviewModel>> response) {
                if (response.isSuccessful()) {

                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ReviewModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));


            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> insertReview(int transactionId, String userId, String reviewText, int fieldId,
                                                float stars){
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.insertReview(transactionId, userId, reviewText, fieldId, stars).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;

    }

    public LiveData<ResponseModel<List<ReviewModel>>> getReview(int fieldId) {
        MutableLiveData<ResponseModel<List<ReviewModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getReview(fieldId).enqueue(new Callback<ResponseModel<List<ReviewModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<ReviewModel>>> call, Response<ResponseModel<List<ReviewModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<ReviewModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));


            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<ReviewModel>> getTotalReview(int fieldId){
        MutableLiveData<ResponseModel<ReviewModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getTotalReview(fieldId).enqueue(new Callback<ResponseModel<ReviewModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ReviewModel>> call, Response<ResponseModel<ReviewModel>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ReviewModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;

    }
 }
