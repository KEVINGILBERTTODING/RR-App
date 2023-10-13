package com.example.rumahraga.ui.activities.onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.ActivityOnBoardingBinding;
import com.example.rumahraga.ui.activities.auth.AuthActivity;
import com.example.rumahraga.util.constans.sharedpref.ConsSharedPref;

public class OnBoardingActivity extends AppCompatActivity {
    private ActivityOnBoardingBinding binding;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean isOnboarding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnBoardingBinding.inflate(getLayoutInflater());
        init();
        validate();
        listener();
        setContentView(binding.getRoot());
    }

    private void init() {
        sharedPreferences = getSharedPreferences(ConsSharedPref.SHARED_PREF_NAME_ONBOARDING, MODE_PRIVATE);
        isOnboarding = sharedPreferences.getBoolean(ConsSharedPref.isOnboarding, false);
        editor = sharedPreferences.edit();
    }

    private void validate() {
        if (isOnboarding == true)  {
            startActivity(new Intent(OnBoardingActivity.this, AuthActivity.class));
            finish();
        }
    }

    private void listener() {
        binding.btnDaftar.setOnClickListener(view -> {
            startActivity(new Intent(OnBoardingActivity.this, AuthActivity.class));
            finish();
            editor.putBoolean(ConsSharedPref.isOnboarding, true);
            editor.apply();
        });
    }
}