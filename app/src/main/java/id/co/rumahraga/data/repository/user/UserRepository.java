package id.co.rumahraga.data.repository.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.UserModel;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private ApiService apiService;
    @Inject
    public UserRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel> updateLocation(String userId, String cityName) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updateLocation(userId, cityName).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<UserModel>> getUser(String userId) {
        MutableLiveData<ResponseModel<UserModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getUser(userId).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));


            }
        });
        return responseModelMutableLiveData;
    }


    public LiveData<ResponseModel> updateUsername(String username, String userId) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updateUsername(username, userId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));


            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updatePassword(String userId, String oldPassword, String newPassword) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updatePassword(userId, oldPassword, newPassword).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));


            }
        });

        return responseModelMutableLiveData;


    }

    public LiveData<ResponseModel> updateProfilePhoto(HashMap map, MultipartBody.Part filePart) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
       apiService.updateProfilePhoto(map, filePart).enqueue(new Callback() {
           @Override
           public void onResponse(Call call, Response response) {
               if (response.isSuccessful()) {
                   responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
               }else {
                   Gson gson = new Gson();
                   ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                   responseModelMutableLiveData.setValue(new ResponseModel(false, responseModel.getMessage(), null));
               }
           }

           @Override
           public void onFailure(Call call, Throwable t) {
               responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

           }
       });

       return responseModelMutableLiveData;
    }
}
