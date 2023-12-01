package id.co.rumahraga.ui.activities.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import id.co.rumahraga.databinding.ActivityMainBinding;
import id.co.rumahraga.ui.fragments.events.EventsFragments;
import id.co.rumahraga.ui.fragments.field.FieldAllFragment;
import id.co.rumahraga.ui.fragments.home.HomeFragment;
import id.co.rumahraga.ui.fragments.profile.ProfileFragment;
import id.co.rumahraga.ui.fragments.transaction.TransactionListFragment;
import id.co.rumahraga.R;

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
                        fragmentTransaction(new EventsFragments());
                        break;

                    case 2:
                        fragmentTransaction(new FieldAllFragment());
                        break;
                    case 3:
                        fragmentTransaction(new TransactionListFragment());
                        break;
                    case 4:
                        fragmentTransaction(new ProfileFragment());
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