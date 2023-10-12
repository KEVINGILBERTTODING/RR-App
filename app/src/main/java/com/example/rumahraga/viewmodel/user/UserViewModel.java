package com.example.rumahraga.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.user.UserRepository;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel

public class UserViewModel extends ViewModel {
    private UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<ResponseModel> updateLocation(String userId, String cityName) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null && cityName != null) {
            return userRepository.updateLocation(userId, cityName);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> getUser(String userId) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null) {
            return userRepository.getUser(userId);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateUsername(String username, String userId) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null && username != null) {
            return userRepository.updateUsername(username, userId);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }
}
