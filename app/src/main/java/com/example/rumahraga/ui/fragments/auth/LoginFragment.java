package com.example.rumahraga.ui.fragments.auth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentLoginBinding;
import com.example.rumahraga.model.ResponseModel;
import com.example.rumahraga.model.UserModel;
import com.example.rumahraga.ui.activities.main.MainActivity;
import com.example.rumahraga.util.constans.other.ConsOther;
import com.example.rumahraga.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        init();



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void init() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }



    private void listener() {
        binding.btnLogin.setOnClickListener(view -> {
            login();
        });
        binding.tvRegister.setOnClickListener(view -> {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuth, new RegisterFragment())
                    .addToBackStack(null).commit();
        });
    }

    private void login() {
        if (binding.etUsername.getText().toString().isEmpty()) {
            binding.etUsername.setError("Tidak boleh kosong");
        }else if (binding.etPassword.getText().toString().isEmpty()) {
            binding.etUsername.setError("Tidak boleh kosong");

        }else {
            authViewModel.login(binding.etUsername.getText().toString(), binding.etPassword.getText().toString())
                    .observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
                        @Override
                        public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                            if (userModelResponseModel.isStatus() == true) {
                                showToast(ConsOther.TOAST_SUCCESS, "Berhasil");
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                            }else {
                                showToast(ConsOther.TOAST_ERR, userModelResponseModel.getMessage());
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