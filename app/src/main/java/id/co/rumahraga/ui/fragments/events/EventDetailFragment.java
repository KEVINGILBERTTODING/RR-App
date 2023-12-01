package id.co.rumahraga.ui.fragments.events;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import id.co.rumahraga.R;

import id.co.rumahraga.databinding.FragmentEventDetailBinding;
import id.co.rumahraga.ui.fragments.field.PreviewImageFragment;


public class EventDetailFragment extends Fragment {
    private FragmentEventDetailBinding binding;
    private String ivEvent;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEventDetailBinding.inflate(inflater, container, false);

        init();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set image
        Glide.with(getContext()).load(ivEvent).skipMemoryCache(false)
                        .diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.ivEvent);
        binding.tvEventName.setText(getArguments().getString("event_name", "-"));
        binding.tvDate.setText(getArguments().getString("date", "-"));
        binding.tvCategory.setText(getArguments().getString("category_name", "-"));
        binding.tvContent.setText(getArguments().getString("content", "-"));


        listener();
    }

    private void init() {
        ivEvent = getArguments().getString("image", "");


    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });

        binding.ivEvent.setOnClickListener(view -> {
            Fragment fragment = new PreviewImageFragment();
            Bundle bundle = new Bundle();
            bundle.putString("image", ivEvent);
            fragment.setArguments(bundle);
            fragmentTransaction(fragment);
        });


    }

    private void fragmentTransaction(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragment)
                .commit();
    }


}