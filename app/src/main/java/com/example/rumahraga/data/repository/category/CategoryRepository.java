package com.example.rumahraga.data.repository.category;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private ApiService apiService;

    @Inject
    public CategoryRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<List<CategoryModel>>> getAllCategory() {
        MutableLiveData<ResponseModel<List<CategoryModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllCategory().enqueue(new Callback<ResponseModel<List<CategoryModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<CategoryModel>>> call, Response<ResponseModel<List<CategoryModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<List<CategoryModel>>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<CategoryModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, "Tidak ada koneksi internet", null));


            }
        });

        return responseModelMutableLiveData;
    }
}
