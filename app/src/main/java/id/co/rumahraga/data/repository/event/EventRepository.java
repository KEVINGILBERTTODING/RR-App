package id.co.rumahraga.data.repository.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.EventModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository {
    private ApiService apiService;
    @Inject
    public EventRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseModel<List<EventModel>>> getEvents(){
        MutableLiveData<ResponseModel<List<EventModel>>>  responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getEvents().enqueue(new Callback<ResponseModel<List<EventModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<EventModel>>> call, Response<ResponseModel<List<EventModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));

                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<EventModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });

    return responseModelMutableLiveData;
    }


}
