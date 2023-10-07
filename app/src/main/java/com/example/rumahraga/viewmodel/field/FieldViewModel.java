package com.example.rumahraga.viewmodel.field;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.field.FieldRepository;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.util.constans.other.ConsOther;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FieldViewModel extends ViewModel {
    private FieldRepository fieldRepository;

    @Inject
    public FieldViewModel(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public LiveData<ResponseModel<List<FieldModel>>> getFieldCloser(String city) {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        if (city != null) {
            return fieldRepository.getFieldCloser(city);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsOther.TOAST_ERR, null));

        }

        return responseModelMutableLiveData;
    }
}
