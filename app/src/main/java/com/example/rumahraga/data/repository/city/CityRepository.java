package com.example.rumahraga.data.repository.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityRepository {
    private ApiService apiService;

    @Inject
    public CityRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<List<CityModel>>> getAllCity() {
        MutableLiveData<ResponseModel<List<CityModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllCity().enqueue(new Callback<ResponseModel<List<CityModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CityModel>>> call, Response<ResponseModel<List<CityModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE,response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CityModel>>> call, Throwable t) {

                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }
}
