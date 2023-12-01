package id.co.rumahraga.data.repository.notification;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.NotificationModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.util.constans.response.ConsResponse;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationRepository {
    private ApiService apiService;

    @Inject
    public NotificationRepository(ApiService apiService) {
        this.apiService = apiService;


    }

    public LiveData<ResponseModel<List<NotificationModel>>> getNotification(String userId, int isRead) {
        MutableLiveData<ResponseModel<List<NotificationModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getNotification(userId, isRead).enqueue(new Callback<ResponseModel<List<NotificationModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<NotificationModel>>> call, Response<ResponseModel<List<NotificationModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<NotificationModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> updateNotification(String userId) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.updateNotification(userId).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() == true) {
                    responseModelMutableLiveData.setValue(new ResponseModel(true, ConsResponse.SUCCESS_MESSAGE, null));
                }else {

                    responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.ERROR_MESSAGE, null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel> deleteNotification(String id) {
        MutableLiveData<ResponseModel> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.deleteNotification(id).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel(false, response.body().getMessage(), null));
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
}
