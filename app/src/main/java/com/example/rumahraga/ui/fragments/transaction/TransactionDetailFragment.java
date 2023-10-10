package com.example.rumahraga.ui.fragments.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentTransactionDetailBinding;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;

import java.text.DecimalFormat;

public class TransactionDetailFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String userId, transactionCode, reason;
    private int status;


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

        if (status == 1) {
            binding.tvStatus.setText("Pembayaran berhasil divalidasi!");
            binding.fab.setText("Lihat Tiket");
        }else if (status == 2) {
            binding.tvStatus.setText("Pembayaran sedang diproses");
            binding.fab.setVisibility(View.GONE);
        }else {
            binding.tvStatus.setText("Pembayaran tidak valid!");

            if (reason != null && !reason.equals("")) {
                binding.fab.setText("Lihat alasan");
            }else {
                binding.fab.setVisibility(View.GONE);
            }
        }



    }
    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, null);

        transactionCode = getArguments().getString("transaction_code", null);
        status = getArguments().getInt("status", 0);
        reason = getArguments().getString("reason", null);


    }

    private void listener() {
    }


}