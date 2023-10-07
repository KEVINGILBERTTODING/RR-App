package com.example.rumahraga.viewmodel.jam;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.jam.JamRepository;
import com.example.rumahraga.model.JamModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class JamViewModel extends ViewModel {
    private JamRepository jamRepository;

    @Inject
    public JamViewModel(JamRepository jamRepository) {
        this.jamRepository = jamRepository;
    }

    public LiveData<ResponseModel<List<JamModel>>> getHour(String fieldId, String date) {
        MutableLiveData<ResponseModel<List<JamModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (fieldId != null && date != null && !fieldId.isEmpty() && !date.isEmpty()) {
            return jamRepository.getHour(fieldId, date);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
