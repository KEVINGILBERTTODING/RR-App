package com.example.rumahraga.viewmodel.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.transaction.TransactionRepository;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;

@HiltViewModel
public class TransactionViewModel extends ViewModel {
    private TransactionRepository transactionRepository;

    @Inject
    public TransactionViewModel(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    public LiveData<ResponseModel> insertTransaction (HashMap map, MultipartBody.Part filePart) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null && filePart != null) {
         return transactionRepository.transaction(map, filePart);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
