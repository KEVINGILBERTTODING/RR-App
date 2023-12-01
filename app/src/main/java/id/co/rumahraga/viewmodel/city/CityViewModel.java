package id.co.rumahraga.viewmodel.city;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import id.co.rumahraga.data.repository.city.CityRepository;
import id.co.rumahraga.model.CityModel;
import id.co.rumahraga.model.ResponseModel;

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
