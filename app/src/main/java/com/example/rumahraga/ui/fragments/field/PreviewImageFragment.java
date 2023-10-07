package com.example.rumahraga.ui.fragments.field;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rumahraga.R;
import com.example.rumahraga.databinding.FragmentPreviewImageBinding;
import com.example.rumahraga.util.constans.response.ConsResponse;

import es.dmoral.toasty.Toasty;

public class PreviewImageFragment extends Fragment {


    private FragmentPreviewImageBinding binding;
    private String image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPreviewImageBinding.inflate(inflater, container, false);
        image = getArguments().getString("image", "");


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listener();

        try {
            Glide.with(getContext()).load(image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(binding.imageView);
        }catch (Exception e) {
            Toasty.error(getContext(), ConsResponse.ERROR_MESSAGE, Toasty.LENGTH_SHORT).show();
        }

    }

    private void listener() {
        binding.btnBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
    }
}