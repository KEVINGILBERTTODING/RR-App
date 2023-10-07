package com.example.rumahraga.data.repository.field;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldRepository {
    private ApiService apiService;

    @Inject
    public FieldRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseModel<List<FieldModel>>> getFieldCloser(String city) {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getFieldCloser(city).enqueue(new Callback<ResponseModel<List<FieldModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FieldModel>>> call, Response<ResponseModel<List<FieldModel>>> response) {
                if (response.isSuccessful()) {

                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsOther.TOAST_SUCCESS, response.body().getData()));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FieldModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, "Tidak ada koneksi internet", null));


            }
        });
        return responseModelMutableLiveData;
    }
}
