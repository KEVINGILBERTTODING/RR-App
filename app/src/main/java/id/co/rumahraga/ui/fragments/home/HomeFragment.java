package id.co.rumahraga.ui.fragments.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import id.co.rumahraga.R;

import id.co.rumahraga.databinding.FragmentHomeBinding;
import id.co.rumahraga.model.BannerModel;
import id.co.rumahraga.model.CategoryModel;
import id.co.rumahraga.model.CityModel;
import id.co.rumahraga.model.FieldModel;
import id.co.rumahraga.model.NotificationModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.listener.ItemClickListener;
import id.co.rumahraga.ui.adapters.category.HomeCategoryAdapter;
import id.co.rumahraga.ui.adapters.fields.FieldMainAdapter;
import id.co.rumahraga.ui.adapters.fields.HomeFieldAdapter;
import id.co.rumahraga.ui.adapters.slider.BannerSliderAdapter;
import id.co.rumahraga.ui.fragments.field.FieldCategoryFragment;
import id.co.rumahraga.ui.fragments.field.FieldDetailFragment;
import id.co.rumahraga.ui.fragments.field.FieldNearbyFragment;
import id.co.rumahraga.ui.fragments.notification.NotificationFragment;
import id.co.rumahraga.util.constans.other.ConsOther;
import id.co.rumahraga.util.constans.response.ConsResponse;
import id.co.rumahraga.util.constans.sharedpref.ConsSharedPref;
import id.co.rumahraga.viewmodel.banner.BannerViewModel;
import id.co.rumahraga.viewmodel.category.CategoryViewModel;
import id.co.rumahraga.viewmodel.city.CityViewModel;
import id.co.rumahraga.viewmodel.field.FieldViewModel;
import id.co.rumahraga.viewmodel.notification.NotificationViewModel;
import id.co.rumahraga.viewmodel.user.UserViewModel;

import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements ItemClickListener {
    private FragmentHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private CategoryViewModel categoryViewModel;
    private String cityName, userId;
    private FieldViewModel fieldViewModel;
    private BannerViewModel bannerViewModel;
    private CityViewModel cityViewModel;
    private UserViewModel userViewModel;
    private ArrayList arrayListCity;
    private Boolean isTextInputLayoutClicked = false;
    private SharedPreferences.Editor editor;
    private NotificationViewModel notificationViewModel;
    private List<NotificationModel> notificationModelList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        init();

        getImageSlider();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getCategory();
        getCity();
        getFieldCloser();
        getRealtimeNotification();



        binding.tvUsername.setText(sharedPreferences.getString(ConsSharedPref.NAME, ""));

        if (cityName != null) {
            binding.searchBar.setQueryHint(cityName);
        }else {
            binding.searchBar.setQueryHint("Cari nama kota");

        }



    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        bannerViewModel = new ViewModelProvider(this).get(BannerViewModel.class);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        cityName = sharedPreferences.getString(ConsSharedPref.CITY, null);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        editor = sharedPreferences.edit();
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

    }

    private void listener(){


        binding.btnCategoriesRefresh.setOnClickListener(view -> {
            getCategory();
        });

        binding.btnSaveMyLocation.setOnClickListener(view -> {
            saveMyLocation();
        });


        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                binding.rlMainField.setVisibility(View.VISIBLE);
                binding.rlMainContent.setVisibility(View.GONE);
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    binding.rlMainContent.setVisibility(View.VISIBLE);
                    binding.rlMainField.setVisibility(View.GONE);

                }
                return false;
            }
        });

        binding.tvSeeAllField.setOnClickListener(view -> {
            fragmentTransaction(new FieldNearbyFragment());
        });


        binding.btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction(new NotificationFragment());
                setReadNotification();
            }
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
                    homeCategoryAdapter.setItemClickListener(HomeFragment.this);

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

    private void initSpinnerLocation() {


        final SearchableSpinner searchableSpinnerCity;

        searchableSpinnerCity= new SearchableSpinner(getContext());
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
                    editor.putString(ConsSharedPref.CITY, cityName);
                    editor.apply();
                    binding.rlLocation.setVisibility(View.GONE);
                    binding.vOverlay.setVisibility(View.GONE);
                    getFieldCloser();
                }else {
                    showToast(ConsOther.TOAST_ERR, responseModel.getMessage());
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

    private void getCity() {
        cityViewModel.getAllCity().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CityModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CityModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    arrayListCity = new ArrayList<>();

                    for (int i = 0; i < listResponseModel.getData().size(); i++){
                        arrayListCity.add(listResponseModel.getData().get(i).getNama());
                    }
                    initSpinnerLocation();
                }
            }
        });
    }


    private void filter(String query) {
        if (query != null) {
            binding.shimmerFieldMain.setVisibility(View.VISIBLE);
            binding.shimmerFieldMain.startShimmer();
            binding.rvMainField.setVisibility(View.GONE);
            binding.rvMainField.setAdapter(null);

                fieldViewModel.getFieldCloser(query).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
                    @Override
                    public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
                        if (listResponseModel.isStatus() == true) {
                            FieldMainAdapter fieldMainAdapter = new FieldMainAdapter(getContext(), listResponseModel.getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvMainField.setLayoutManager(linearLayoutManager);
                            binding.rvMainField.setAdapter(fieldMainAdapter);
                            binding.rvMainField.setHasFixedSize(true);
                            binding.shimmerFieldMain.setVisibility(View.GONE);
                            binding.tvEmpty.setVisibility(View.GONE);
                            fieldMainAdapter.setItemClickListener(HomeFragment.this);
                            binding.rvMainField.setVisibility(View.VISIBLE);
                        }else {
                            binding.shimmerFieldMain.setVisibility(View.GONE);
                            binding.rvMainField.setVisibility(View.VISIBLE);
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                            binding.tvEmpty.setText("Lapangan tidak ditemukan");
                            showToast(ConsResponse.ERROR_MESSAGE, listResponseModel.getMessage());
                        }
                    }
                });

        }

    }



    private void getFieldCloser() {
        if (cityName != null) {
            binding.vOverlay.setVisibility(View.GONE);
            binding.rlLocation.setVisibility(View.GONE);
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
                            binding.tvFieldEmpty.setText("Tidak ada lapangan terdekat");

                        }
                    }
                });
            }else {
                binding.shimmerField.setVisibility(View.GONE);
                binding.lrFieldEmpty.setVisibility(View.VISIBLE);
                binding.tvFieldEmpty.setText("Lokasi tidak ditemukan");
                showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
            }
        }else {
            binding.vOverlay.setVisibility(View.VISIBLE);
            binding.rlLocation.setVisibility(View.VISIBLE);

        }

    }

    private void getNotification() {

        notificationViewModel.getAllNotification(userId, 0).observe(this, new Observer<ResponseModel<List<NotificationModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<NotificationModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true && listResponseModel != null) {

                        notificationModelList = listResponseModel.getData();
                        binding.rlTotalNotification.setVisibility(View.VISIBLE);
                        binding.tvNotification.setText(String.valueOf(notificationModelList.size()));

                }else {
                    binding.rlTotalNotification.setVisibility(View.GONE);

                }
            }
        });
    }

    private void setReadNotification() {
        notificationViewModel.updateNotification(userId).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.isStatus() == true) {

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

    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMain, fragment)
                .commit();
    }

    private void getRealtimeNotification() {


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getNotification();
                getRealtimeNotification();
            }
        }, 1000L);
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

        }else if (type.equals("category")) {
            CategoryModel categoryModel= (CategoryModel) object;
            Fragment fragment = new FieldCategoryFragment();
            Bundle arg = new Bundle();
            arg.putString("name", categoryModel.getName());
            arg.putString("category_id", categoryModel.getCategory_id());
            fragment.setArguments(arg);
            fragmentTransaction(fragment);
        }


    }
}