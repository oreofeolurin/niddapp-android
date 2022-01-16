package com.example.niddapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.niddapp.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private MainController controller;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        Log.d("TAG", "about to attach");

        controller = new MainController(this);

        return binding.getRoot();

    }

    private void submitForm() {
        boolean hasErrors = false;
        String deviceName = binding.deviceNameInputEditText.getText().toString().trim();
        String deviceId = binding.deviceIdInputEditText.getText().toString().trim();

        if(deviceName.equals("")) {
            hasErrors = true;
            binding.deviceNameInputLayout.setError("Device Name is required");
        }
        if(deviceId.equals("")) {
            hasErrors = true;
            binding.deviceIdInputLayout.setError("Device ID is required");
        }

        if(!hasErrors){
            binding.deviceNameInputLayout.setError(null);
            binding.deviceIdInputLayout.setError(null);

            controller.saveDevice(deviceId, deviceName);

            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.submitBtn.setOnClickListener( _view -> submitForm());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}