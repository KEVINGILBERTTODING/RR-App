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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentFieldDetailBinding;
import com.example.rumahraga.model.BookedModel;
import com.example.rumahraga.model.FieldModel;
import com.example.rumahraga.model.JamModel;
import com.example.rumahraga.model.PaymentMethodModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.ReviewModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.booked.BookedAdapter;
import com.example.rumahraga.ui.adapters.jam.JamAdapter;
import com.example.rumahraga.ui.adapters.spinner.SpinnerPaymentMethodAdapter;
import com.example.rumahraga.ui.fragments.transaction.PaymentFragment;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.field.FieldViewModel;
import com.example.rumahraga.viewmodel.jam.JamViewModel;
import com.example.rumahraga.viewmodel.payment.PaymentViewModel;
import com.example.rumahraga.viewmodel.review.ReviewViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sahana.horizontalcalendar.OnDateSelectListener;
import com.sahana.horizontalcalendar.model.DateModel;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class FieldDetailFragment extends Fragment implements ItemClickListener {

    private FragmentFieldDetailBinding binding;
    private FieldViewModel fieldViewModel;
    private ReviewViewModel reviewViewModel;
    private SharedPreferences sharedPreferences;
    private String userId, fieldId, image, date, paymentId, mitraId, transactionCode;
    private JamViewModel jamViewModel;
    private JamAdapter jamAdapter;
    private BookedAdapter bookedAdapter;
    private int rentPrice, totalPrice, finalTotalTransaction;
    private List<BookedModel> bookedModelList;
    private BottomSheetBehavior bottomSheetBehaviorCheckOut;
    private PaymentViewModel paymentViewModel;
    private SpinnerPaymentMethodAdapter spinnerPaymentMethodAdapter;



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
        getDataBooked();
        initBottomSheetCheckout();
        countTotalTransaction();
        getPaymentMethod();
        bottomSheetBehaviorCheckOut.setState(BottomSheetBehavior.STATE_HIDDEN);
        // init transaction code
        String timeStamp = String.valueOf(System.currentTimeMillis());
        transactionCode = "TRX" + userId + timeStamp;
    }

    private void init() {
        fieldViewModel = new ViewModelProvider(this).get(FieldViewModel.class);
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        fieldId = getArguments().getString("field_id", null);
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
        jamViewModel = new ViewModelProvider(this).get(JamViewModel.class);
        bookedModelList = new ArrayList<>();
        bookedAdapter = new BookedAdapter(getContext(),bookedModelList);
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);


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
                bookedModelList.clear();
                binding.rvBookedDate.setAdapter(null);
                countTotalTransaction();
                binding.swipeRefresh.setRefreshing(false);
                getPaymentMethod();
            }
        });

        binding.btnDetailTransaksi.setOnClickListener(view -> {
            showBottomSheetCheckOut();
        });

        binding.vOverlay.setOnClickListener(view -> {
            hiddenBottomSheetCheckout();
        });

        binding.btnCheckOut.setOnClickListener(view -> {
            if (paymentId != null) {
                Fragment fragment = new PaymentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("payment_id", paymentId);
                bundle.putInt("final_price", finalTotalTransaction);
                bundle.putString("field_id", fieldId);
                bundle.putString("mitra_id", mitraId);
                bundle.putString("transaction_code", transactionCode);
                bundle.putSerializable("item_list", (Serializable) bookedModelList);
                fragment.setArguments(bundle);
                fragmentTransaction(fragment);
            }else {
                showToast(ConsOther.TOAST_ERR, "Anda belum memilih metode pembayaran");
            }

        });

        binding.spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                paymentId = spinnerPaymentMethodAdapter.getPaymentId(i);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                int firstItemIndex = 0;
                paymentId = spinnerPaymentMethodAdapter.getPaymentId(firstItemIndex);
            }
        });
    }

    private void countTotalTransaction() {
        if (bookedModelList != null && bookedModelList.size() > 0) {
            totalPrice = bookedModelList.size() * rentPrice;
            finalTotalTransaction = totalPrice + 2500;

            formatRupiah(binding.tvTotalPrice, totalPrice);
            binding.btnDetailTransaksi.setEnabled(true);
            binding.btnDetailTransaksi.setClickable(true);
            binding.tvTotalHour.setText(String.valueOf(bookedModelList.size()) + " Jam");
            formatRupiah(binding.tvFinalTotalTransaction, finalTotalTransaction);
            formatRupiah(binding.tvCheckOutPrice, finalTotalTransaction);

        }else {
            binding.tvTotalPrice.setText("Rp. 0");
            binding.btnDetailTransaksi.setEnabled(false);
            binding.btnDetailTransaksi.setClickable(false);
        }
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
                        mitraId = fieldModelResponseModel.getData().getMitra_id();
                        Glide.with(getContext()).load(fieldModelResponseModel.getData().getImage())
                                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true)
                                .into(binding.ivField);

                        image = fieldModelResponseModel.getData().getImage();
                        rentPrice = fieldModelResponseModel.getData().getPrice();
                        formatRupiah(binding.tvRentPrice, fieldModelResponseModel.getData().getPrice());





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
                        jamAdapter = new JamAdapter(getContext(), listResponseModel.getData());
                        binding.rvDate.setLayoutManager(gridLayoutManager);
                        binding.rvDate.setAdapter(jamAdapter);
                        binding.rvDate.setHasFixedSize(true);
                        jamAdapter.setItemClickListener(FieldDetailFragment.this);
                        binding.shimmerDate.setVisibility(View.GONE);
                        binding.rvDate.setVisibility(View.VISIBLE);

                        if (bookedModelList != null && bookedModelList.size() > 0) {
                            for(int i =0; i < listResponseModel.getData().size(); i++) {
                                for (BookedModel bookedModel : bookedModelList) {
                                    if (bookedModel.getOrder_date().equals(date) && bookedModel.getJam().equals(listResponseModel.getData().get(i).getJam())) {
                                        listResponseModel.getData().get(i).setIs_available(2);
                                    }
                                }
                            }
                        }


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

    private void getPaymentMethod() {
        paymentViewModel.getAllPayment().observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<PaymentMethodModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<PaymentMethodModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    spinnerPaymentMethodAdapter = new SpinnerPaymentMethodAdapter(getContext(), listResponseModel.getData());
                    binding.spinnerPaymentMethod.setAdapter(spinnerPaymentMethodAdapter);

                }else {
                    showToast(ConsOther.TOAST_ERR, "Gagal memuat metode pembayaran");
                    binding.btnCheckOut.setEnabled(false);
                    binding.btnCheckOut.setClickable(false);
                }
            }
        });
    }

    private void initBottomSheetCheckout() {
        bottomSheetBehaviorCheckOut = BottomSheetBehavior.from(binding.bottomSheetCheckOut);
        bottomSheetBehaviorCheckOut.setHideable(true);
        bottomSheetBehaviorCheckOut.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    hiddenBottomSheetCheckout();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void hiddenBottomSheetCheckout() {
        bottomSheetBehaviorCheckOut.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.vOverlay.setVisibility(View.GONE);
    }

    private void showBottomSheetCheckOut() {
        bottomSheetBehaviorCheckOut.setState(BottomSheetBehavior.STATE_COLLAPSED);
        binding.vOverlay.setVisibility(View.VISIBLE);
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



    private void getDataBooked() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rvBookedDate.setLayoutManager(linearLayoutManager);
        binding.rvBookedDate.setAdapter(bookedAdapter);
    }


    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.frameMain, fragment)
                .commit();
    }

    private void formatRupiah(TextView textView, int nominal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        textView.setText("Rp. "+decimalFormat.format(nominal));
    }

    @Override
    public void onItemClickListener(String type, int postion, Object object) {
        // adapter jam diklik
        if (type.equals("hour")) {
            JamModel  jamModel = (JamModel) object;
            if (jamModel.getIs_available() == 1) {
                jamModel.setIs_available(2);
                jamAdapter.notifyItemChanged(postion);
                jamAdapter.notifyItemRangeChanged(postion, jamAdapter.getItemCount());
                // menambahkan daftar booked
                bookedModelList.add(new BookedModel(date, transactionCode, jamModel.getJam_id(), rentPrice, jamModel.getJam()));
                bookedAdapter.notifyDataSetChanged();

                // refresh item
                getDataBooked();

                // total transaksi

                countTotalTransaction();

            }else if (jamModel.getIs_available() == 2) {
                jamModel.setIs_available(1);
                jamAdapter.notifyItemChanged(postion);
                jamAdapter.notifyItemRangeChanged(postion, jamAdapter.getItemCount());

                // cek dan hapus data booked
                for (BookedModel bookedModel : bookedModelList) {
                    if (bookedModel.getOrder_date().equals(date) && bookedModel.getJam().equals(jamModel.getJam())) {
                        bookedModelList.remove(bookedModel);
                        break; //
                    }
                }
                bookedAdapter.notifyDataSetChanged();

                // refresh item
                getDataBooked();

                // total transaksi
                countTotalTransaction();
            }

        }


    }
}