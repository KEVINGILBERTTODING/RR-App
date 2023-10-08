package com.example.rumahraga.ui.fragments.field;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentFieldAllBinding;
import com.example.rumahraga.model.CategoryModel;
import com.example.rumahraga.model.CityModel;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.fields.FieldMainAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.viewmodel.category.CategoryViewModel;
import com.example.rumahraga.viewmodel.city.CityViewModel;
import com.example.rumahraga.viewmodel.field.FieldViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class FieldAllFragment extends Fragment implements ItemClickListener {
    private FieldViewModel fieldViewModel;
    private CityViewModel cityViewModel;
    String locationName, categoryName;

    private FragmentFieldAllBinding binding;
    private List<FieldModel> fieldModelList;
    private FieldMainAdapter fieldMainAdapter;
    private List<String> cityModelList, categoryModelList;
    private CategoryViewModel categoryViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFieldAllBinding.inflate(inflater, container, false);

        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
        getField();
        getAllCity();
        getAllCategory();


    }

    private void init() {
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);


    }

    private void listener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getField();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        binding.searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        binding.btnFilter.setOnClickListener(view -> {
            initFilterDialog();
        });


    }

    private void getField(){
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.rvField.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);



//            fieldViewModel.getAl().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
//                @Override
//                public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
//                    binding.rvField.setVisibility(View.VISIBLE);
//                    binding.shimmer.setVisibility(View.GONE);
//                    if (listResponseModel.isStatus() == true) {
//                        if (listResponseModel.getData().size() > 0) {
//                            fieldModelList = listResponseModel.getData();
//                            fieldMainAdapter = new FieldMainAdapter(getContext(), fieldModelList);
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//                            binding.rvField.setLayoutManager(linearLayoutManager);
//                            binding.rvField.setAdapter(fieldMainAdapter);
//                            binding.rvField.setHasFixedSize(true);
//                            fieldMainAdapter.setItemClickListener(FieldAllFragment.this);
//                        }else {
//                            binding.tvEmpty.setVisibility(View.VISIBLE);
//                        }
//                    }else {
//                        showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
//                    }
//                }
//            });


    }

    private void filterField(String cityName, String cagoryName){
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.rvField.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);
         binding.rvField.setAdapter(null);




            fieldViewModel.filterField(cityName, cagoryName).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
                    categoryName = null;
                    locationName = null;

                    binding.rvField.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    if (listResponseModel.isStatus() == true) {
                        if (listResponseModel.getData().size() > 0) {
                            fieldModelList = listResponseModel.getData();
                            fieldMainAdapter = new FieldMainAdapter(getContext(), fieldModelList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            binding.rvField.setLayoutManager(linearLayoutManager);
                            binding.rvField.setAdapter(fieldMainAdapter);
                            binding.rvField.setHasFixedSize(true);
                            fieldMainAdapter.setItemClickListener(FieldAllFragment.this);
                        }else {
                            binding.tvEmpty.setVisibility(View.VISIBLE);
                        }
                    }else {
                        binding.tvEmpty.setVisibility(View.VISIBLE);
                        binding.tvEmpty.setText("Tidak ada data");
                        showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
                    }
                }
            });


    }
    private void getAllCity() {
        cityViewModel.getAllCity().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CityModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CityModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {

                    cityModelList = new ArrayList<>();
                    for (int i = 0; i < listResponseModel.getData().size(); i++) {
                        cityModelList.add(listResponseModel.getData().get(i).getNama());
                    }
                }else {
                    showToast(ConsOther.TOAST_ERR, "Gagal mengambil lokasi");
                }
            }
        });
    }

    private void getAllCategory() {
        categoryViewModel.getAllCategory().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<CategoryModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<CategoryModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    categoryModelList = new ArrayList<>();
                    for (int i = 0; i <listResponseModel.getData().size(); i++ ) {
                        categoryModelList.add(listResponseModel.getData().get(i).getName());
                    }
                }else {
                    showToast(ConsOther.TOAST_ERR, "Gagal mengambil kategori olahraga");
                }
            }
        });
    }



    private void filter(String query) {
        if (fieldModelList != null) {
            ArrayList<FieldModel> filteredList = new ArrayList<>();
            for (FieldModel item : fieldModelList) {
                if (fieldModelList != null && item.getName().toLowerCase().contains(query.toLowerCase())){
                    filteredList.add(item);
                }

                fieldMainAdapter.filter(filteredList);
                if (!filteredList.isEmpty()) {
                    fieldMainAdapter.filter(filteredList);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setText("Lapangan tidak ditemukan");
                }

            }
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

    private void initFilterDialog() {
        Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_filter);
        final SmartMaterialSpinner spinnerLocation, spinnerCategory;
        final Button btnFilter = dialog.findViewById(R.id.btnFilter);
        spinnerLocation = dialog.findViewById(R.id.spinnerLocation);
        spinnerCategory = dialog.findViewById(R.id.spinnerCategory);
        spinnerLocation.setItem(cityModelList);
        spinnerCategory.setItem(categoryModelList);
        dialog.show();


        // listener
        spinnerLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showToast(ConsOther.TOAST_NORMAL, cityModelList.get(i));
                locationName = cityModelList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                showToast(ConsOther.TOAST_NORMAL, categoryModelList.get(i));
                categoryName = categoryModelList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnFilter.setOnClickListener(view -> {
            if (locationName == null) {
                showToast(ConsOther.TOAST_ERR, "Anda belum memilih lokasi");
            }else if (categoryName == null) {
                showToast(ConsOther.TOAST_ERR, "Anda belum memilih kategori");
            }else {
                filterField(locationName, categoryName);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        FieldModel fieldModel = (FieldModel) object;
        if (fieldModel.getField_id() != null) {
            Fragment fragment = new FieldDetailFragment();
            Bundle arg = new Bundle();
            arg.putString("field_id", fieldModel.getField_id());
            fragment.setArguments(arg);
            fragmentTransaction(fragment);
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }

    }
}