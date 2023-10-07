package com.example.rumahraga.ui.fragments.field;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentFieldDetailBinding;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.field.FieldViewModel;
import com.example.rumahraga.viewmodel.review.ReviewViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class FieldDetailFragment extends Fragment {

    private FragmentFieldDetailBinding binding;
    private FieldViewModel fieldViewModel;
    private ReviewViewModel reviewViewModel;
    private SharedPreferences sharedPreferences;
    private String userId, fieldId;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFieldDetailBinding.inflate(inflater, container, false);

        init();
       return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();

        getFieldDetail();
        getTotalRating();

    }

    private void init() {
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        fieldId = getArguments().getString("field_id", null);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);

    }

    private void listener() {
    }

    private void getFieldDetail() {
        if (fieldId != null) {
            fieldViewModel.getFieldById(fieldId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<FieldModel>>() {
                @Override
                public void onChanged(ResponseModel<FieldModel> fieldModelResponseModel) {
                    if (fieldModelResponseModel.isStatus() == true && fieldModelResponseModel.getData() != null) {
                        Glide.with(getContext()).load(fieldModelResponseModel.getData().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                                .into(binding.ivField);
                        binding.tvAddress.setText(fieldModelResponseModel.getData().getAddress());
                        binding.tvFieldName.setText(fieldModelResponseModel.getData().getName());

                    }else {
                        showToast(ConsOther.TOAST_ERR, fieldModelResponseModel.getMessage());
                    }
                }
            });

        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }
    }

    private void getTotalRating() {
       try {
           reviewViewModel.getTotalRating(fieldId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<ReviewModel>>() {
               @Override
               public void onChanged(ResponseModel<ReviewModel> reviewModelResponseModel) {
                    if (reviewModelResponseModel.isStatus() == true) {
                        binding.ratingBar.setRating(reviewModelResponseModel.getData().getTotal_rating());
                        binding.tvRating.setText(String.valueOf(reviewModelResponseModel.getData().getTotal_rating()));
                    }else {
                        binding.ratingBar.setRating(0.0f);
                        binding.tvRating.setText("0");
                    }
               }
           });
       }catch (Exception e) {
           showToast(ConsOther.TOAST_ERR, e.getMessage());
       }
    }
    private void showToast(String type, String message) {
        if (type.equals(ConsOther.TOAST_SUCCESS)) {
            Toasty.success(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else if (type.equals(ConsOther.TOAST_NORMAL)){
            Toasty.normal(getContext(), message, Toasty.LENGTH_SHORT).show();
        }else {
            Toasty.error(getContext(), message, Toasty.LENGTH_SHORT).show();

        }
    }

    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMain, fragment)
                .commit();
    }

}