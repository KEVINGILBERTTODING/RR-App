package com.example.rumahraga.ui.fragments.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentProfileBinding;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.city.CityViewModel;
import com.example.rumahraga.viewmodel.user.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {


    private FragmentProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String userId, username, name, cityName;
    private UserViewModel userViewModel;
    private List<CityModel> cityModelList;
    private BottomSheetBehavior bottomSheetBehaviorCity;
    private CityViewModel cityViewModel;
    private SearchableSpinner searchableSpinnerCity;
    private ArrayList<String> arrayListCity = new ArrayList<>();
    private Boolean isTextInputLayoutClicked = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

   

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getUserData();

        binding.tvUsername.setText(username);

        initBottomSheetCity();
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_HIDDEN);
        getCity();
        listener();
    }

    private void listener() {
        binding.swipeReresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserData();
                getCity();
                binding.swipeReresh.setRefreshing(false);
            }
        });
        binding.cvCity.setOnClickListener(view -> {
            showBtmSheetCity();
        });
        binding.btnSaveMyLocation.setOnClickListener(view -> {
            if (cityName != null) {
                saveSharedPref(ConsSharedPref.CITY, cityName);
                saveMyLocation();
            }
        });


    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        username = sharedPreferences.getString(ConsSharedPref.USERNAME, "");
        editor = sharedPreferences.edit();
        name = sharedPreferences.getString(ConsSharedPref.NAME, "");
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);


    }

    private void getUserData() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.profileImage.setVisibility(View.GONE);
        userViewModel.getUser(userId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
            @Override
            public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                if (userModelResponseModel.isStatus() == true) {
                    Glide.with(getContext()).load(userModelResponseModel.getData().getProfile_picture())
                            .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                            .into(binding.profileImage);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.profileImage.setVisibility(View.VISIBLE);

                }else {
                    showToast(ConsOther.TOAST_ERR, ConsOther.TOAST_ERR);
                }
            }
        });

    }

    private void getCity() {
        cityViewModel.getAllCity().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CityModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CityModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    for (CityModel item : listResponseModel.getData()) {
                        arrayListCity.add(item.getNama());
                    }
                    initSpinnerLocation();


                }else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                    binding.btnSaveMyLocation.setEnabled(false);
                }
            }
        });
    }

    private void initBottomSheetCity() {
        bottomSheetBehaviorCity = BottomSheetBehavior.from(binding.bottomSheetCity);
        bottomSheetBehaviorCity.setHideable(true);
        bottomSheetBehaviorCity.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hideBtmSheetCity();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void showBtmSheetCity() {
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
    }

    private void hideBtmSheetCity() {
        bottomSheetBehaviorCity.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void initSpinnerLocation() {


        searchableSpinnerCity = new SearchableSpinner(getContext());
        searchableSpinnerCity.setWindowTitle("Pilih Kota Anda");
        searchableSpinnerCity.setSpinnerListItems(arrayListCity);

        // listener spinner
        searchableSpinnerCity.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                if (isTextInputLayoutClicked) {
                    binding.textInputSpinnerCity.getEditText().setText(selectedString);
                    cityName = selectedString;
                    showToast(ConsOther.TOAST_NORMAL, selectedString);
                }


            }
        });

        binding.textInputSpinnerCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTextInputLayoutClicked = true;
                searchableSpinnerCity.setHighlightSelectedItem(true);
                searchableSpinnerCity.show();

            }
        });


    }

    private void saveMyLocation() {
        userViewModel.updateLocation(userId, cityName).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.isStatus() == true) {
                    showToast(ConsOther.TOAST_SUCCESS, "Berhasil mengubah lokasi");
                    hideBtmSheetCity();
                }else {
                    showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                }
            }
        });
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
    private void saveSharedPref(String sharedName, String value) {
        editor.putString(sharedName, value);
        editor.apply();
    }
}