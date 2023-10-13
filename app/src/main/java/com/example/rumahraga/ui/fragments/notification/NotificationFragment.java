package com.example.rumahraga.ui.fragments.notification;

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
import com.example.rumahraga.databinding.FragmentNotificationBinding;
import com.example.rumahraga.model.NotificationModel;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.listener.ItemClickListener;
import com.example.rumahraga.ui.adapters.notification.NotificationAdapter;
import com.example.rumahraga.ui.fragments.transaction.TransactionDetailFragment;
import com.example.rumahraga.ui.fragments.transaction.TransactionListFragment;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.util.constans.response.ConsResponse;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.notification.NotificationViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class NotificationFragment extends Fragment implements ItemClickListener {
    private FragmentNotificationBinding binding;
    private NotificationViewModel notificationViewModel;
    private SharedPreferences sharedPreferences;
    private String userId;
    private List<NotificationModel> notificationModelList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       binding = FragmentNotificationBinding.inflate(inflater, container, false);
       init();
       return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getNotification();
        listener();

    }

    private void listener() {

        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

        binding.btnDelete.setOnClickListener(view -> {
            if (notificationModelList != null) {
                deleteNotification();
            }else {
                showToast(ConsOther.TOAST_NORMAL, "Tidak ada notifikasi");
            }
        });

    }

    private void init() {
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(ConsSharedPref.USER_ID, "");
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
    }

    private void getNotification() {
        binding.rvNotif.setVisibility(View.GONE);
        binding.shimmer.setVisibility(View.VISIBLE);
        binding.shimmer.startShimmer();
        binding.tvEmpty.setVisibility(View.GONE);
        notificationViewModel.getAllNotification(userId, 1).observe(getViewLifecycleOwner(), new Observer<ResponseModel<List<NotificationModel>>>() {
            @Override
            public void onChanged(ResponseModel<List<NotificationModel>> listResponseModel) {
                if (listResponseModel.isStatus() == true) {
                    notificationModelList = listResponseModel.getData();
                    NotificationAdapter notificationAdapter = new NotificationAdapter(getContext(), notificationModelList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    binding.rvNotif.setLayoutManager(linearLayoutManager);
                    binding.rvNotif.setAdapter(notificationAdapter);
                    binding.rvNotif.setVisibility(View.VISIBLE);
                    binding.tvEmpty.setVisibility(View.GONE);
                    notificationAdapter.setItemClickListener(NotificationFragment.this);
                    binding.shimmer.setVisibility(View.GONE);

                }else {
                    binding.tvEmpty.setText("Tidak ada notifikasi");
                    binding.tvEmpty.setVisibility(View.VISIBLE);
                    binding.shimmer.setVisibility(View.GONE);
                    binding.rvNotif.setVisibility(View.GONE);
                    showToast(ConsOther.TOAST_ERR, listResponseModel.getMessage());
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


    private void deleteNotification() {
        binding.rvNotif.setAdapter(null);
        binding.rvNotif.setVisibility(View.GONE);
        binding.tvEmpty.setVisibility(View.VISIBLE);
        binding.tvEmpty.setText("Tidak ada notifikasi");
        notificationViewModel.deleteNotification(userId).observe(getViewLifecycleOwner(), new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.isStatus() == true) {

                }else {

                }
            }
        });
    }
    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment)
                .commit();
    }



    @Override
    public void onItemClickListener(String type, int positon, Object object) {
        NotificationModel notificationModel = (NotificationModel) object;
        if (notificationModel != null) {
            fragmentTransaction(new TransactionListFragment());

        }else {
            showToast(ConsOther.TOAST_ERR, ConsResponse.ERROR_MESSAGE);
        }
    }
}