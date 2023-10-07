package com.example.rumahraga.ui.fragments.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentHomeBinding;
import com.example.rumahraga.model.BannerModel;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.category.HomeCategoryAdapter;
import com.example.rumahraga.ui.adapters.fields.HomeFieldAdapter;
import com.example.rumahraga.ui.adapters.slider.BannerSliderAdapter;
import com.example.rumahraga.ui.fragments.field.FieldDetailFragment;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.banner.BannerViewModel;
import com.example.rumahraga.viewmodel.category.CategoryViewModel;
import com.example.rumahraga.viewmodel.field.FieldViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements ItemClickListener {
    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private CategoryViewModel categoryViewModel;
    private FusedLocationProviderClient fusedLocationClient;
    private String cityName;
    private FieldViewModel fieldViewModel;
    private BannerViewModel bannerViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        init();
        checkStatusGps();
        getImageSlider();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getCategory();


        binding.tvUsername.setText(sharedPreferences.getString(ConsSharedPref.NAME, ""));

    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);

    }

    private void listener(){
        binding.lrFieldEmpty.setOnClickListener(view -> {
            checkStatusGps();

        });

        binding.btnCategoriesRefresh.setOnClickListener(view -> {
            getCategory();
        });

    }

    private void getCategory() {
        binding.shimmerCategories.setVisibility(View.VISIBLE);
        binding.shimmerCategories.startShimmer();
        binding.rvCategory.setVisibility(View.GONE);
        binding.lrCategoriesEmpty.setVisibility(View.GONE);
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CategoryModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CategoryModel>> categoryModelResponseModel) {
                if (categoryModelResponseModel.isStatus() == true) {
                    HomeCategoryAdapter homeCategoryAdapter = new HomeCategoryAdapter(getContext(),categoryModelResponseModel.getData());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    binding.rvCategory.setLayoutManager(linearLayoutManager);
                    binding.rvCategory.setAdapter(homeCategoryAdapter);
                    binding.rvCategory.setHasFixedSize(true);

                    binding.shimmerCategories.setVisibility(View.GONE);
                    binding.rvCategory.setVisibility(View.VISIBLE);
                    binding.lrCategoriesEmpty.setVisibility(View.GONE);
                }else {
                    showToast(ConsOther.TOAST_ERR, categoryModelResponseModel.getMessage());
                    binding.shimmerCategories.setVisibility(View.GONE);
                    binding.rvCategory.setVisibility(View.GONE);
                    binding.lrCategoriesEmpty.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private void getImageSlider() {
        bannerViewModel.getAllBanner().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<BannerModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<BannerModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true ) {
                    BannerSliderAdapter bannerSliderAdapter = new BannerSliderAdapter(getContext(), listResponseModel.getData());
                    binding.imageSlider.setSliderAdapter(bannerSliderAdapter);
                    binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                    binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                    binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                    binding.imageSlider.setIndicatorSelectedColor(Color.WHITE);
                    binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                    binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
                    binding.imageSlider.startAutoCycle();
                }else {
                    showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
                }
            }
        });
    }



    private void getFieldCloser(String cityName) {
        binding.shimmerField.setVisibility(View.VISIBLE);
        binding.shimmerField.startShimmer();
        binding.rvField.setVisibility(View.GONE);
        binding.lrFieldEmpty.setVisibility(View.GONE);
        if (cityName != null) {
            fieldViewModel.getFieldCloser(cityName).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
                    if (listResponseModel.isStatus() == true) {
                        HomeFieldAdapter homeFieldAdapter = new HomeFieldAdapter(getContext(), listResponseModel.getData());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        binding.rvField.setAdapter(homeFieldAdapter);
                        binding.rvField.setLayoutManager(linearLayoutManager);
                        binding.rvField.setHasFixedSize(true);
                        homeFieldAdapter.setItemClickListener(HomeFragment.this);

                        binding.shimmerField.setVisibility(View.GONE);
                        binding.rvField.setVisibility(View.VISIBLE);
                        binding.lrFieldEmpty.setVisibility(View.GONE);
                    }else {
                        binding.shimmerField.setVisibility(View.GONE);
                        binding.lrFieldEmpty.setVisibility(View.VISIBLE);
                        binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");
                        showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
                        showToast(ConsResponse.ERROR_MESSAGE, listResponseModel.getMessage());
                    }
                }
            });
        }else {
            binding.shimmerField.setVisibility(View.GONE);
            binding.lrFieldEmpty.setVisibility(View.VISIBLE);
            binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");
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

    private void checkStatusGps() {

        // check status gps
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setMessage("GPS tidak aktif");
            dialog.setPositiveButton("Aktifkan", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    binding.shimmerField.setVisibility(View.GONE);
                    binding.lrFieldEmpty.setVisibility(View.VISIBLE);
                    binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");
                }
            });
            dialog.setNegativeButton("Batal", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    binding.shimmerField.setVisibility(View.GONE);
                    binding.lrFieldEmpty.setVisibility(View.VISIBLE);
                    binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");
                }
            });
            dialog.show();



        }else {
            // CHECK PERMISSION GET LOCATION
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }

            // GET CITY NAME
            fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(requireContext());
                    try {
                        cityName = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0).getLocality();
                        getFieldCloser(cityName);
                    } catch (Exception e) {
                        e.printStackTrace();
                        binding.shimmerField.setVisibility(View.GONE);
                        binding.lrFieldEmpty.setVisibility(View.VISIBLE);
                        binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");

                    }
                }
            });

        }



    }

    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        // field on click
        if (type.equals("field")) {
            FieldModel fieldModel = (FieldModel) object;
            Fragment fragment = new FieldDetailFragment();
            Bundle arg = new Bundle();
            arg.putString("field_id", fieldModel.getField_id());
            fragment.setArguments(arg);
            fragmentTransaction(fragment);

        }


    }
}