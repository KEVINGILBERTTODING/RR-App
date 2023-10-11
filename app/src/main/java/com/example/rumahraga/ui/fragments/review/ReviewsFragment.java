package com.example.rumahraga.ui.fragments.review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentReviewsBinding;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.ui.adapters.review.ReviewAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.viewmodel.review.ReviewViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class ReviewsFragment extends Fragment {
    private FragmentReviewsBinding binding;
    private ReviewViewModel reviewViewModel;
    private int fieldId;
    private List<ReviewModel> reviewModelList;
    private ReviewAdapter reviewAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewsBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    private void init() {
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        fieldId = getArguments().getInt("field_id", 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllReviews();
        listener();

    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllReviews();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getAllReviews() {
        if (fieldId != 0) {
            binding.shimmer.setVisibility(View.VISIBLE);
            binding.shimmer.startShimmer();
            binding.rvReviews.setVisibility(View.GONE);
            binding.tvEmpty.setVisibility(View.GONE);
            reviewViewModel.getAllReviews(fieldId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<ReviewModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<ReviewModel>> listResponseModel) {
                    binding.rvReviews.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    if (listResponseModel.isStatus() == true) {
                        reviewModelList = listResponseModel.getData();
                        reviewAdapter = new ReviewAdapter(getContext(), reviewModelList);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        binding.rvReviews.setLayoutManager(linearLayoutManager);
                        binding.rvReviews.setAdapter(reviewAdapter);
                        binding.rvReviews.setHasFixedSize(true);

                        binding.tvEmpty.setVisibility(View.GONE);
                    }else {
                        showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
                        binding.tvEmpty.setVisibility(View.VISIBLE);

                    }
                }
            });
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
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
}
