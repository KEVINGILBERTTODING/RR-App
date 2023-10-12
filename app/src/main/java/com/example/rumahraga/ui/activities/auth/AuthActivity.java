package com.example.rumahraga.ui.activities.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.rumahraga.R;
import com.example.rumahraga.databinding.ActivityAuthBinding;
import com.example.rumahraga.ui.fragments.auth.LoginFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // minta periziinan untuk mengakses lokasi
         checkPermission();


        fragmentTransaction(new LoginFragment());
    }



    private void fragmentTransaction(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameAuth, fragment).commit();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // belum ada ijin
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                // tampilkan dialog minta ijin
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                // minta ijin
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {



        }
    }




}