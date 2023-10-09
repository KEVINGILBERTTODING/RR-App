package com.example.rumahraga.viewmodel.payment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.payment.PaymentRepository;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;

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
}
