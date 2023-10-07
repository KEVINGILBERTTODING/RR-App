package com.example.rumahraga.viewmodel.review;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rumahraga.data.repository.review.ReviewRepository;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.util.constans.response.ConsResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ReviewViewModel extends ViewModel {
    private ReviewRepository reviewRepository;


    @Inject
    public ReviewViewModel(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public LiveData<ResponseModel<ReviewModel>> getTotalRating(String id) {
        MutableLiveData<ResponseModel<ReviewModel>> responseModelMutableLiveData = new MutableLiveData<>();
        if (id != null & !id.isEmpty()) {
            return reviewRepository.getTotalRating(id);
        }else {
            responseModelMutableLiveData.setValue(new ResponseModel<>(false, ConsResponse.ERROR_MESSAGE, null));
        }

        return responseModelMutableLiveData;
    }
}
