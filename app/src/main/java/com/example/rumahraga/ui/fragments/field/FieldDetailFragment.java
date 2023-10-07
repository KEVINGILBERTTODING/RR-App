package com.example.rumahraga.ui.fragments.field;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentFieldDetailBinding;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.JamModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.ui.adapters.jam.JamAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.field.FieldViewModel;
import com.example.rumahraga.viewmodel.jam.JamViewModel;
import com.example.rumahraga.viewmodel.review.ReviewViewModel;
import com.sahana.horizontalcalendar.OnDateSelectListener;
import com.sahana.horizontalcalendar.model.DateModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class FieldDetailFragment extends Fragment {

    private FragmentFieldDetailBinding binding;
    private FieldViewModel fieldViewModel;
    private ReviewViewModel reviewViewModel;
    private SharedPreferences sharedPreferences;
    private String userId, fieldId, image, date;
    private JamViewModel jamViewModel;



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
        jamViewModel = new ViewModelProvider(this).get(JamViewModel.class);

    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        binding.ivField.setOnClickListener(view -> {
            if (image != null) {
                Fragment fragment = new PreviewImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("image", image);
                fragment.setArguments(bundle);
                fragmentTransaction(fragment);
            }else {
                showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
            }

        });

        binding.horizontalCalendar.setOnDateSelectListener(new OnDateSelectListener() {
            @Override
            public void onSelect(DateModel dateModel) {
                date = dateModel.year + "-" + dateModel.monthNumber + "-" + dateModel.day;
                getHour(date);
            }
        });

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFieldDetail();
                getTotalRating();
                getHour(date);
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getFieldDetail() {
        if (fieldId != null) {

            binding.shimmerMain.setVisibility(View.VISIBLE);
            binding.lrMain.setVisibility(View.GONE);
            binding.shimmerMain.startShimmer();
            fieldViewModel.getFieldById(fieldId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<FieldModel>>() {
                @Override
                public void onChanged(ResponseModel<FieldModel> fieldModelResponseModel) {
                    if (fieldModelResponseModel.isStatus() == true && fieldModelResponseModel.getData() != null) {
                        binding.lrMain.setVisibility(View.VISIBLE);
                        binding.shimmerMain.setVisibility(View.GONE);
                        Glide.with(getContext()).load(fieldModelResponseModel.getData().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                                .into(binding.ivField);

                        image = fieldModelResponseModel.getData().getImage();



                        if (!fieldModelResponseModel.getData().getAddress().isEmpty()) {
                            binding.tvAddress.setText(fieldModelResponseModel.getData().getAddress());
                        }else {
                            binding.tvAddress.setText("-");
                        }

                        if (!fieldModelResponseModel.getData().getName().isEmpty()) {
                            binding.tvFieldName.setText(fieldModelResponseModel.getData().getName());

                        }else {
                            binding.tvFieldName.setText("-");
                        }

                        // jika field tidak tersedia
                        if (fieldModelResponseModel.getData().getIs_available() == 1) {
                            binding.cvStatus.setCardBackgroundColor(getContext().getColor(R.color.main));
                            binding.tvStatus.setText("Buka");

                        }else {
                            binding.cvStatus.setCardBackgroundColor(getContext().getColor(R.color.red));
                            binding.tvStatus.setText("Tutup");
                        }

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
           showToast(ConsOther.TOAST_ERR, ConsResponse.SERVER_ERROR);
       }
    }

    private void getHour(String date) {
        try {

            if (fieldId != null && date != null) {
                binding.shimmerDate.setVisibility(View.VISIBLE);
                binding.rvDate.setVisibility(View.GONE);
            jamViewModel.getHour(fieldId, date).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<JamModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<JamModel>> listResponseModel) {


                    if (listResponseModel.isStatus() == true) {
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext() , 4);
                        JamAdapter jamAdapter = new JamAdapter(getContext(), listResponseModel.getData());
                        binding.rvDate.setLayoutManager(gridLayoutManager);
                        binding.rvDate.setAdapter(jamAdapter);
                        binding.rvDate.setHasFixedSize(true);
                        binding.shimmerDate.setVisibility(View.GONE);
                        binding.rvDate.setVisibility(View.VISIBLE);
                    }else {
                        showToast(ConsOther.TOAST_ERR, "Gagal mengambil tanggal");
                    }
                }
            });
            }
        }catch (Exception e) {
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


    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMain, fragment)
                .commit();
    }

}