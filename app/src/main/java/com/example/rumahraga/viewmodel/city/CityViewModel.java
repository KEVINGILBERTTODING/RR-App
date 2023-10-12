package com.example.rumahraga.viewmodel.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.city.CityRepository;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.ResponseModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CityViewModel extends ViewModel {
    private CityRepository cityRepository;

    @Inject
    public CityViewModel(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public LiveData<ResponseModel<List<CityModel>>> getAllCity() {
        return cityRepository.getAllCity();
    }
}
