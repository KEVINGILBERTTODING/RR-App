package id.co.rumahraga.ui.fragments.review;

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

import id.co.rumahraga.databinding.FragmentReviewsBinding;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.ReviewModel;
import id.co.rumahraga.ui.adapters.review.ReviewAdapter;
import id.co.rumahraga.util.constans.other.ConsOther;
import id.co.rumahraga.util.constans.response.ConsResponse;
import id.co.rumahraga.viewmodel.review.ReviewViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
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
    private BottomSheetBehavior bottomSheetFilter;



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
        initBottomSheetFilter();
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);

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

        binding.btnFilter.setOnClickListener(view -> {
            showBottomsheetFilter();
        });


        binding.vOverlay.setOnClickListener(view -> {
            hideBottomsheetFilter();
        });

        binding.btnStar1.setOnClickListener(view -> {
            filter(1);
            hideBottomsheetFilter();
        });

        binding.btnStar2.setOnClickListener(view -> {
            filter(2);
            hideBottomsheetFilter();
        });

        binding.btnStar3.setOnClickListener(view -> {
            filter(3);
            hideBottomsheetFilter();
        });

        binding.btnStar4.setOnClickListener(view -> {
            filter(4);
            hideBottomsheetFilter();
        });
        binding.btnStar5.setOnClickListener(view -> {
            filter(6);
            hideBottomsheetFilter();
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

    private void initBottomSheetFilter() {
        bottomSheetFilter = BottomSheetBehavior.from(binding.bottomSheetFilter);
        bottomSheetFilter.setHideable(true);
        bottomSheetFilter.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBottomsheetFilter();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBottomsheetFilter() {
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBottomsheetFilter() {
        bottomSheetFilter.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void filter(float rating) {
        if (reviewModelList != null) {
            ArrayList<ReviewModel> filteredList = new ArrayList<>();
            float tolerance = 0.5f; // Toleransi kesalahan dalam perbandingan
            for (ReviewModel item : reviewModelList) {
                if (item != null && Math.abs(item.getStars() - rating) <= tolerance) {
                    filteredList.add(item);
                }
            }

            reviewAdapter.filterRating(filteredList);

            if (!filteredList.isEmpty()) {
                binding.tvEmpty.setVisibility(View.GONE);
                reviewAdapter.filterRating(filteredList);

            } else {
                binding.tvEmpty.setText("Tidak ada rating");
                binding.tvEmpty.setVisibility(View.VISIBLE);
            }
        } else {
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
