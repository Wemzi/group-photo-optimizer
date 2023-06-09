package com.cxteam.groupphotooptimizer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.cxteam.groupphotooptimizer.databinding.FragmentGalleryBinding;

import java.io.IOException;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageClassificationHelper imageClassifier = null;
        try {
            imageClassifier = new ImageClassificationHelper(binding.getRoot().getContext());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Integer tippedNumber = 0;
        try {
            int imageID = R.drawable.mnist_three;
            binding.imageView.setImageDrawable(getResources().getDrawable(imageID));
            tippedNumber=imageClassifier.classifyImage(imageID);
            System.out.println(tippedNumber);
            TextView text = binding.textviewSecond.findViewById(R.id.textview_second);
            text.setText(tippedNumber.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        binding.buttonSecond.setOnClickListener(view1 -> NavHostFragment.findNavController(GalleryFragment.this)
                .navigate(R.id.action_GalleryFragment_to_MenuFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}