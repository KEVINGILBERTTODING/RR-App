package id.co.rumahraga.data.repository.transaction;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import id.co.rumahraga.data.remote.ApiService;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.TransactionDetailModel;
import id.co.rumahraga.util.constans.response.ConsResponse;
import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionDetailRepository {
    private ApiService apiService;

    @Inject
    public TransactionDetailRepository(ApiService apiService) {
        this.apiService = apiService;
    }


    public LiveData<ResponseModel<List<TransactionDetailModel>>> getTransactionDetail(String id) {
        MutableLiveData<ResponseModel<List<TransactionDetailModel>>> responseModelMutableLiveData = new MutableLiveData<>();
        apiService.getDetailTransaction(id).enqueue(new Callback<ResponseModel<List<TransactionDetailModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<List<TransactionDetailModel>>> call, Response<ResponseModel<List<TransactionDetailModel>>> response) {
                if (response.isSuccessful()) {
                    responseModelMutableLiveData.setValue(new ResponseModel<>(true, ConsResponse.SUCCESS_MESSAGE, response.body().getData()));
                }else {
                    Gson gson = new Gson();
                    ResponseModel responseModel = gson.fromJson(response.errorBody().charStream(), ResponseModel.class);
                    responseModelMutableLiveData.setValue(new ResponseModel<>(false, responseModel.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<List<TransactionDetailModel>>> call, Throwable t) {
                responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.SERVER_ERROR, null));

            }
        });
        return responseModelMutableLiveData;
    }
}
