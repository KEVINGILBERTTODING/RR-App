package id.co.rumahraga.viewmodel.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.co.rumahraga.data.repository.user.UserRepository;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.UserModel;
import id.co.rumahraga.util.constans.response.ConsResponse;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import okhttp3.MultipartBody;

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

    public LiveData<ResponseModel> updatePassword(String userId, String oldPassword, String newPassword) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (userId != null && oldPassword != null && newPassword != null) {
            return userRepository.updatePassword(userId, oldPassword, newPassword);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateProfilePhoto(HashMap map, MultipartBody.Part filePart) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        if (map != null && filePart != null) {
            return userRepository.updateProfilePhoto(map, filePart);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }


}
