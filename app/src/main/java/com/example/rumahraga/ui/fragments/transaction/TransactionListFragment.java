package com.example.rumahraga.ui.fragments.transaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentTransactionListBinding;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.TransactionModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.transactions.TransactionsAdapter;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.transaction.TransactionViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;


@AndroidEntryPoint
public class TransactionListFragment extends Fragment implements ItemClickListener {

    private FragmentTransactionListBinding binding;
    private SharedPreferences sharedPreferences;
    private TransactionViewModel transactionViewModel;
    private String userId;
    private List<TransactionModel> transactionModels;
    private TransactionsAdapter transactionsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionListBinding.inflate(inflater, container, false);


        init();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMyTransactions();
        listener();

    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, null);
        transactionViewModel = new ViewModelProvider(this).get(TransactionViewModel.class);
    }

    private void listener() {
        binding.btnShort.setOnClickListener(view -> {
            if (transactionModels != null) {
                Collections.reverse(transactionModels);
                transactionsAdapter.notifyDataSetChanged();
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

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMyTransactions();
                binding.swipeRefresh.setRefreshing(false);
            }
        });
    }



    private void getMyTransactions() {
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.tvEmpty.setVisibility(View.GONE);
        binding.rvTransactions.setVisibility(View.GONE);

        transactionViewModel.getMyTransactions(userId).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<TransactionModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<TransactionModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    transactionModels = listResponseModel.getData();
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    transactionsAdapter = new TransactionsAdapter(getContext(), transactionModels);
                    binding.rvTransactions.setLayoutManager(linearLayoutManager);
                    binding.rvTransactions.setAdapter(transactionsAdapter);
                    binding.rvTransactions.setHasFixedSize(true);

                    // click listener
                    transactionsAdapter.setItemClickListener(TransactionListFragment.this);


                    // hide shimmer
                    binding.rvTransactions.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.tvEmpty.setVisibility(View.GONE);
                }else {
                    binding.rvTransactions.setVisibility(View.GONE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setText("Tidak ada transaksi");
                    showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());

                }
            }
        });

    }

    private void filter(String newText) {
        if (transactionModels != null) {
            ArrayList<TransactionModel> filteredList = new ArrayList<>();
            for (TransactionModel item : transactionModels) {
                if (item != null && item.getField_name().toLowerCase().contains(newText.toLowerCase())) {
                    filteredList.add(item);
                }
                transactionsAdapter.filter(filteredList);

                if (!filteredList.isEmpty()) {
                    binding.tvEmpty.setVisibility(View.GONE);
                    transactionsAdapter.filter(filteredList);
                }else {
                    binding.tvEmpty.setText("Lapangan tidak ditemukan");
                    binding.tvEmpty.setVisibility(View.VISIBLE);

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


    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        TransactionModel transactionModel = (TransactionModel) object;
        Fragment fragment = new TransactionDetailFragment();
        Bundle arg = new Bundle();
        arg.putString("transaction_code", transactionModel.getTransaction_code());
        arg.putString("payment_name", transactionModel.getPayment_name());
        arg.putString("order_date", transactionModel.getCreated_at());
        arg.putInt("status", transactionModel.getStatus());
        arg.putInt("total_price", transactionModel.getTotal_price());
        arg.putString("reason", transactionModel.getReason());
        fragment.setArguments(arg);
        fragmentTransaction(fragment);
    }
}