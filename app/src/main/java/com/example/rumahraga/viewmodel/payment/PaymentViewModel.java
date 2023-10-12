package com.example.rumahraga.viewmodel.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.payment.PaymentRepository;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class PaymentViewModel extends ViewModel {
    private PaymentRepository paymentRepository;

    @Inject
    public PaymentViewModel(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public LiveData<ResponseModel<List<PaymentMethodModel>>> getAllPayment(){
        return paymentRepository.getAllPayment();
    }

    public LiveData<ResponseModel<PaymentMethodModel>> getPaymentDetail(String id) {
        MutableLiveData<ResponseModel<PaymentMethodModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null && !id.isEmpty()) {
            return paymentRepository.getPaymentDetail(id);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
