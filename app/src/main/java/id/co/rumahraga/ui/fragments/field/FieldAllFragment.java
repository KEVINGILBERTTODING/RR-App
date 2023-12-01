package id.co.rumahraga.ui.fragments.field;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import id.co.rumahraga.R;

import id.co.rumahraga.databinding.FragmentFieldAllBinding;
import id.co.rumahraga.model.CategoryModel;
import id.co.rumahraga.model.CityModel;
import id.co.rumahraga.model.FieldModel;
import id.co.rumahraga.model.ResponseModel;
import id.co.rumahraga.model.listener.ItemClickListener;
import id.co.rumahraga.ui.adapters.fields.FieldMainAdapter;
import id.co.rumahraga.util.constans.other.ConsOther;
import id.co.rumahraga.util.constans.response.ConsResponse;
import id.co.rumahraga.viewmodel.category.CategoryViewModel;
import id.co.rumahraga.viewmodel.city.CityViewModel;
import id.co.rumahraga.viewmodel.field.FieldViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.leo.searchablespinner.SearchableSpinner;
import com.leo.searchablespinner.interfaces.OnItemSelectListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class FieldAllFragment extends Fragment implements ItemClickListener {
    private FieldViewModel fieldViewModel;
    private CityViewModel cityViewModel;
    private String locationName, categoryName;
    private Boolean isTextInputLayoutClicked = false;

    private FragmentFieldAllBinding binding;
    private List<FieldModel> fieldModelList;
    private FieldMainAdapter fieldMainAdapter;
    private ArrayList<String> arrayListCity, arrayListCategory;
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
        getAllField();
        getAllCity();
        getAllCategory();


    }

    private void init() {
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        cityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        arrayListCity = new ArrayList<>();
        arrayListCategory = new ArrayList<>();


    }

    private void listener() {
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllField();
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

        binding.btnShort.setOnClickListener(view -> {
           if (fieldModelList != null) {
               // urutkan dari bawah ke atas
               Collections.reverse(fieldModelList);
               fieldMainAdapter.notifyDataSetChanged();
           }

        });


    }

    private void getAllField(){
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.rvField.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.GONE);

            fieldViewModel.getAllField().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<FieldModel>>>() {
                @Override
                public void onChanged(ResponseModel<List<FieldModel>> listResponseModel) {
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
                        showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
                    }
                }
            });


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

                            FieldMainAdapter fieldMainAdapter = new FieldMainAdapter(getContext(), listResponseModel.getData());
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

                    arrayListCity = new ArrayList<>();
                    for (int i = 0; i < listResponseModel.getData().size(); i++) {
                        arrayListCity.add(listResponseModel.getData().get(i).getNama());
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
                    arrayListCategory = new ArrayList<>();
                    for (int i = 0; i <listResponseModel.getData().size(); i++ ) {
                        arrayListCategory.add(listResponseModel.getData().get(i).getName());
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

        final Button btnFilter = dialog.findViewById(R.id.btnFilter);

        // custom spinner
        final TextInputLayout textInputLayoutSpinnerCity,  textInputLayoutSpinnerCategory;
        textInputLayoutSpinnerCity = dialog.findViewById(R.id.textInputSpinnerCity);
        textInputLayoutSpinnerCategory = dialog.findViewById(R.id.textInputSpinnerCategory);
        textInputLayoutSpinnerCity.setOnKeyListener(null);
        textInputLayoutSpinnerCategory.setOnKeyListener(null);

        final SearchableSpinner searchableSpinnerCity,searchableSpinnerCategory;
        searchableSpinnerCity= new SearchableSpinner(getContext());
        searchableSpinnerCategory= new SearchableSpinner(getContext());
        searchableSpinnerCity.setWindowTitle("Pilih Kota");
        searchableSpinnerCategory.setWindowTitle("Pilih Jenis Olahraga");
        searchableSpinnerCity.setSpinnerListItems(arrayListCity);
        searchableSpinnerCategory.setSpinnerListItems(arrayListCategory);


        // listener spinner
        searchableSpinnerCity.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                if (isTextInputLayoutClicked) {
                    textInputLayoutSpinnerCity.getEditText().setText(selectedString);
                    locationName = selectedString;
                    showToast(ConsOther.TOAST_NORMAL, selectedString);
                }


            }
        });


        textInputLayoutSpinnerCity.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTextInputLayoutClicked = true;
                searchableSpinnerCity.setHighlightSelectedItem(true);
                searchableSpinnerCity.show();

            }
        });

        searchableSpinnerCategory.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void setOnItemSelectListener(int position, @NotNull String selectedString) {
                if (isTextInputLayoutClicked) {
                    textInputLayoutSpinnerCategory.getEditText().setText(selectedString);
                    categoryName = selectedString;
                    showToast(ConsOther.TOAST_NORMAL, selectedString);
                }


            }
        });


        textInputLayoutSpinnerCategory.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isTextInputLayoutClicked = true;
                searchableSpinnerCategory.setHighlightSelectedItem(true);
                searchableSpinnerCategory.show();

            }
        });


        dialog.show();



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