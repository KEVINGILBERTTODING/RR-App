package com.example.rumahraga.data.repository.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

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
 }
