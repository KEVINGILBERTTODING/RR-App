package com.example.rumahraga.ui.fragments.ticket;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.databinding.FragmentTicketFragmmentBinding;
import com.example.rumahraga.model.TransactionDetailModel;
import com.example.rumahraga.ui.adapters.ticket.TransactionTicketAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;

import java.text.DecimalFormat;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import es.dmoral.toasty.Toasty;

public class TicketFragment extends Fragment {

    private FragmentTicketFragmmentBinding binding;
    private SharedPreferences sharedPreferences;
    private String username, email, transactionCode;
    private Bitmap bitmap;
    private QRGEncoder qrgEncoder;
    private List<TransactionDetailModel> transactionDetailModelList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTicketFragmmentBinding.inflate(inflater, container, false);
        init();


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.tvTransactionCode.setText(transactionCode);
        binding.tvUsername.setText(username);
        binding.tvEmail.setText(email);
        binding.tvAddress.setText(getArguments().getString("field_address", "-"));
        binding.tvFieldName.setText(getArguments().getString("field_name", "-"));
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        binding.tvTotalPrice.setText("Rp. " + decimalFormat.format(getArguments().getInt("total_price", 0)));
        setQrcode();
        getDetailOrder();




        listener();

    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(ConsSharedPref.NAME, "");
        email = sharedPreferences.getString(ConsSharedPref.EMAIL, "");
        transactionCode = getArguments().getString("transaction_code", "");
        qrgEncoder = new QRGEncoder(transactionCode, null, QRGContents.Type.TEXT, 1200);
        transactionDetailModelList = (List<TransactionDetailModel>) getArguments().getSerializable("transaction_detail");


    }


    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }

    private void getDetailOrder() {
        if (transactionDetailModelList != null) {
            TransactionTicketAdapter transactionTicketAdapter = new TransactionTicketAdapter(getContext(), transactionDetailModelList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            binding.rvItem.setLayoutManager(linearLayoutManager);
            binding.rvItem.setAdapter(transactionTicketAdapter);
            binding.rvItem.setHasFixedSize(true);
        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }

    }

    private void setQrcode() {
        if (!transactionCode.equals("")) {
            qrgEncoder.setColorBlack(Color.WHITE);
            qrgEncoder.setColorWhite(Color.BLACK);

            // set qrcode
            bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            binding.ivTicket.setImageBitmap(bitmap);

        }else {
            showToast(ConsOther.TOAST_ERR, "Gagal generate kode transaksi");

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