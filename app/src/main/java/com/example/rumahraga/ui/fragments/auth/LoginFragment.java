package com.example.rumahraga.ui.fragments.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;
import com.example.rumahraga.viewmodel.auth.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;
import es.dmoral.toasty.Toasty;

@AndroidEntryPoint
public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        init();
        validateLogin();



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();
    }

    private void init() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sharedPreferences = getContext().getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
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
        } else if (binding.etUsername.getText().toString().length() < 8) {
            binding.etUsername.setError("Tidak boleh kurang dari 8 karakter");
        }else if (binding.etPassword.getText().toString().length() < 8) {
            binding.etPassword.setError("Tidak boleh kurang dari 8 karakter");
        } else if (binding.etUsername.getText().toString().matches("[0-9]+")) {
            binding.etUsername.setError("Username tidak boleh mengandung angka");
        }else {
            authViewModel.login(binding.etUsername.getText().toString(), binding.etPassword.getText().toString())
                    .observe(getViewLifecycleOwner(), new Observer<ResponseModel<UserModel>>() {
                        @Override
                        public void onChanged(ResponseModel<UserModel> userModelResponseModel) {
                            if (userModelResponseModel.isStatus() == true) {
                                showToast(ConsOther.TOAST_SUCCESS, "Berhasil");
                                startActivity(new Intent(getContext(), MainActivity.class));
                                getActivity().finish();
                                saveUserInfo(userModelResponseModel.getData().getUser_id(), userModelResponseModel.getData().getEmail(),
                                        userModelResponseModel.getData().getUsername(), userModelResponseModel.getData().getName());
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

    private void saveUserInfo(String userId, String email, String username, String name) {
        editor.putBoolean(ConsSharedPref.LOGIN_SESSION, true);
        editor.putString(ConsSharedPref.USER_ID, userId);
        editor.putString(ConsSharedPref.EMAIL, email);
        editor.putString(ConsSharedPref.USERNAME, username);
        editor.putString(ConsSharedPref.NAME, name);
        editor.apply();
    }

    private void validateLogin() {
        if (sharedPreferences.getBoolean(ConsSharedPref.LOGIN_SESSION, false) == true) {
         startActivity(new Intent(getContext(), MainActivity.class));
         getActivity().finish();
        }
    }


}