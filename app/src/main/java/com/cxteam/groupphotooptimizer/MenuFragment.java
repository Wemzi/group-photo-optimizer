package com.cxteam.groupphotooptimizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import com.cxteam.groupphotooptimizer.databinding.FragmentMenuBinding;

public class MenuFragment extends Fragment {
    private FragmentMenuBinding binding;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        binding.buttonGallery.setOnClickListener(galleryOnClickListener);
        binding.buttonCamera.setOnClickListener(cameraOnClickListener);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private View.OnClickListener galleryOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NavController controller = NavHostFragment.findNavController(MenuFragment.this);
            controller.navigate(R.id.action_MenuFragment_to_GalleryFragment);
        }
    };

    private View.OnClickListener cameraOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent myIntent = new Intent(getContext(), CameraActivity.class);
            startActivity(myIntent);
        }
    };

}