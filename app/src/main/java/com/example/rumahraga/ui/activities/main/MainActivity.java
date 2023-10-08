package com.example.rumahraga.ui.activities.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.ActivityMainBinding;
import com.example.rumahraga.ui.fragments.field.FieldAllFragment;
import com.example.rumahraga.ui.fragments.home.HomeFragment;

import dagger.hilt.android.AndroidEntryPoint;
import me.ibrahimsn.lib.OnItemSelectedListener;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listener();

        fragmentTransaction(new HomeFragment());
    }

    private void listener() {
        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                switch (i) {
                    case 0:
                        fragmentTransaction(new HomeFragment());
                        break;
                    case 1:
                        fragmentTransaction(new FieldAllFragment());
                        break;


                }
                return false;
            }
        });
    }

    private void fragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment).commit();
    }





}