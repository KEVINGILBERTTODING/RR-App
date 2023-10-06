package com.example.rumahraga.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    @Inject
    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<UserModel>> login(String username, String password) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.login(username, password).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                    }else {
                        responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));

                    }


                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, "Tidak ada koneksi internet", null));

            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> register(String username, String email, String name, String password) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.register(username, email, name, password).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        responseModelMutableLiveData.setValue(new ResponseModel<>(true, response.body().getMessage(), response.body().getData()));
                    }else {
                        responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));

                    }


                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, "Tidak ada koneksi internet", null));

            }
        });

        return responseModelMutableLiveData;
    }
}
