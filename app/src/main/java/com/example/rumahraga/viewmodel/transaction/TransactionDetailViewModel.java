package com.example.rumahraga.viewmodel.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.transaction.TransactionDetailRepository;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.TransactionDetailModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


@HiltViewModel
public class TransactionDetailViewModel extends ViewModel {
    private TransactionDetailRepository transactionDetailRepository;

    @Inject
    public TransactionDetailViewModel(TransactionDetailRepository transactionDetailRepository) {
        this.transactionDetailRepository = transactionDetailRepository;
    }

    public LiveData<ResponseModel<List<TransactionDetailModel>>> getDetailTransaction(String id){
        MutableLiveData<ResponseModel<List<TransactionDetailModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null && !id.isEmpty()) {
            return transactionDetailRepository.getTransactionDetail(id);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
