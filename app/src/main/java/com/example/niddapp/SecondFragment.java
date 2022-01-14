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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private DatabaseReference devicesCollection;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        Log.d("TAG", "about to attach");
        binding.submitBtn.setOnClickListener(view -> submitForm());

        devicesCollection = FirebaseDatabase.getInstance().getReference().child("NiddApp").child("devices");

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

            sendToFireBase(deviceId, deviceName);

            NavHostFragment.findNavController(SecondFragment.this)
                    .navigate(R.id.action_SecondFragment_to_FirstFragment);
        }
    }

    private void sendToFireBase(String deviceId, String deviceName) {
        HashMap<String, Object> map  = new HashMap<>();
        map.put(deviceId, new DeviceModel(deviceId, deviceName));

        devicesCollection.updateChildren(map);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

     /**   binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        }); **/
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}