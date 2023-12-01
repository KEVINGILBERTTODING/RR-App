package id.co.rumahraga.viewmodel.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import id.co.rumahraga.data.repository.auth.AuthRepository;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.UserModel;
import id.co.rumahraga.util.constans.response.ConsResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;
    @Inject
    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<ResponseModel<UserModel>> login (String username, String password) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (username != null && password != null) {
            return authRepository.login(username, password);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> register (String username, String email, String name, String password) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (username != null && email != null && name != null  && password != null) {
            return authRepository.register(username, email, name, password);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }
        return responseModelMutableLiveData;
    }
}
