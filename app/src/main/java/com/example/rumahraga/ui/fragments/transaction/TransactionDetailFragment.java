package com.example.rumahraga.ui.fragments.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentTransactionDetailBinding;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.TransactionDetailModel;
import com.example.rumahraga.ui.adapters.transactions.TransactionDetailAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.transaction.TransactionDetailViewModel;

import java.text.DecimalFormat;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class TransactionDetailFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String userId, transactionCode, reason;
    private int status;
    private TransactionDetailViewModel transactionDetailViewModel;
    private TransactionDetailAdapter transactionDetailAdapter;
    private List<TransactionDetailModel> transactionDetailModelist;


    private FragmentTransactionDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionDetailBinding.inflate(inflater, container, false);

        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();

        binding.tvMetodePembayaran.setText(getArguments().getString("payment_name", null));
        binding.tvDate.setText(getArguments().getString("order_date", null));
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        binding.tvFinalTotalTransaction.setText("Rp. " + decimalFormat.format(getArguments().getInt("total_price", 0)));

        if (status == 1) { // transaction success
            binding.tvStatus.setText("Pembayaran berhasil divalidasi!");
            binding.fab.setText("Lihat Tiket");
            binding.fab.setVisibility(View.VISIBLE);
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.success_anim);
        }else if (status == 2) {
            binding.tvStatus.setText("Pembayaran sedang diproses");
            binding.fab.setVisibility(View.GONE);
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.progress_anim);
            binding.tvFinalTotalTransaction.setTextColor(getContext().getColor(R.color.yellow));

        }else {
            binding.tvStatus.setText("Pembayaran tidak valid!");

            if (reason != null && !reason.equals("")) {
                binding.fab.setText("Lihat alasan");
                binding.fab.setVisibility(View.VISIBLE);
            }else {
                binding.fab.setVisibility(View.GONE);
            }
            binding.tvFinalTotalTransaction.setTextColor(getContext().getColor(R.color.red));
            // set animation
            binding.lottieAnimation.setAnimation(R.raw.failed_animation);
        }


         binding.tvTransactionCode.setText(transactionCode);

        getDetailTransaction();





    }
    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, null);

        transactionCode = getArguments().getString("transaction_code", null);
        status = getArguments().getInt("status", 0);
        reason = getArguments().getString("reason", null);
        transactionDetailViewModel = new ViewModelProvider(this).get(TransactionDetailViewModel.class);


    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    private void getDetailTransaction() {
        if (transactionCode != null) {
            binding.shimmerMain.setVisibility(View.VISIBLE);
            binding.shimmerMain.startShimmer();
            binding.lrMain.setVisibility(View.GONE);
            transactionDetailViewModel.getDetailTransaction(transactionCode).observe(getViewLifecycleOwner(),
                    new Observer<ResponseModel<List<TransactionDetailModel>>>() {
                        @Override
                        public void onChanged(ResponseModel<List<TransactionDetailModel>> listResponseModel) {
                            if (listResponseModel.isStatus() == true) {
                                transactionDetailModelist = listResponseModel.getData();
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                transactionDetailAdapter = new TransactionDetailAdapter(getContext(), transactionDetailModelist);
                                binding.rvItem.setAdapter(transactionDetailAdapter);
                                binding.rvItem.setLayoutManager(linearLayoutManager);
                                binding.rvItem.setHasFixedSize(true);

                                // rekayasa tampilan
                                binding.shimmerMain.setVisibility(View.GONE);
                                binding.lrMain.setVisibility(View.VISIBLE);



                            }else {
                                showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());

                            }
                        }
                    });
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