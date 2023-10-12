package com.example.rumahraga.data.repository.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.rumahraga.data.remote.ApiService;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.google.android.gms.common.api.Api;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentRepository {
    private ApiService apiService;

    @Inject
    public PaymentRepository (ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseModel<List<PaymentMethodModel>>> getAllPayment() {
        MutableLiveData<ResponseModel<List<PaymentMethodModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllPayment().enqueue(new Callback<ResponseModel<List<PaymentMethodModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<PaymentMethodModel>>> call, Response<ResponseModel<List<PaymentMethodModel>>> response) {
                if (response.isSuccessful() == true) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<PaymentMethodModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<PaymentMethodModel>> getPaymentDetail(String id) {
        MutableLiveData<ResponseModel<PaymentMethodModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getPaymentDetail(id).enqueue(new Callback<ResponseModel<PaymentMethodModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<PaymentMethodModel>> call, Response<ResponseModel<PaymentMethodModel>> response) {
                if (response.isSuccessful() == true) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<PaymentMethodModel>> call, Throwable t) {

                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));
            }
        });

        return responseModelMutableLiveData;
    }
}
