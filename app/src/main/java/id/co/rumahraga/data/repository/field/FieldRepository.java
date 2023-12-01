package id.co.rumahraga.data.repository.field;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.FieldModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.util.constans.other.ConsOther;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FieldRepository {
    private ApiService apiService;

    @Inject
    public FieldRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseModel<List<FieldModel>>> getFieldCloser(String city) {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getFieldCloser(city).enqueue(new Callback<ResponseModel<List<FieldModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FieldModel>>> call, Response<ResponseModel<List<FieldModel>>> response) {
                if (response.isSuccessful()) {

                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsOther.TOAST_SUCCESS, response.body().getData()));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FieldModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, "Tidak ada koneksi internet", null));


            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<FieldModel>> getFieldById(String id) {
        MutableLiveData<ResponseModel<FieldModel>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getFieldById(id).enqueue(new Callback<ResponseModel<FieldModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<FieldModel>> call, Response<ResponseModel<FieldModel>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsOther.TOAST_SUCCESS, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel  responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<FieldModel>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<FieldModel>>> getFieldByCategory(String id) {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getFieldByCategory(id).enqueue(new Callback<ResponseModel<List<FieldModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FieldModel>>> call, Response<ResponseModel<List<FieldModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FieldModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }

        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<FieldModel>>> filterField(String cityName, String categoryName) {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.filterField(cityName, categoryName).enqueue(new Callback<ResponseModel<List<FieldModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FieldModel>>> call, Response<ResponseModel<List<FieldModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FieldModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));


            }
        });
        return responseModelMutableLiveData;
    }

    public LiveData<ResponseModel<List<FieldModel>>> getAllField() {
        MutableLiveData<ResponseModel<List<FieldModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getAllField().enqueue(new Callback<ResponseModel<List<FieldModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<FieldModel>>> call, Response<ResponseModel<List<FieldModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<FieldModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));


            }
        });
        return responseModelMutableLiveData;
    }

}
