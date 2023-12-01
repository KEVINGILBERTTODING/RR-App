package id.co.rumahraga.data.repository.jam;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.JamModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JamRepository {
    private ApiService apiService;

    @Inject
    public JamRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<ResponseModel<List<JamModel>>> getHour(String fieldId, String date){
        MutableLiveData<ResponseModel<List<JamModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getHour(fieldId, date).enqueue(new Callback<ResponseModel<List<JamModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<JamModel>>> call, Response<ResponseModel<List<JamModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<JamModel>>> call, Throwable t) {

                responseModelMutableLiveData.setValue(new ResponseModel<>(false,ConsResponse.SERVER_ERROR, null));

            }
        });

        return responseModelMutableLiveData;
    }

}
