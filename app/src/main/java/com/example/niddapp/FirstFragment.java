package com.example.niddapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.niddapp.databinding.FragmentFirstBinding;
import com.example.niddapp.device.Device;
import com.example.niddapp.device.DeviceAdapter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {
    private ArrayList<Device> deviceArrayList;
    private DeviceAdapter deviceAdapter;

    private LocationManager locationManager;
    private Location location;
    private int PERMISSION_CODE = 1;
    private String cityName;

    private FragmentFirstBinding binding;
    private MainController controller;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        controller = new MainController(this);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        location = getLocation(view.getContext());
        cityName = getCityName();

        if(cityName != null) {
            setWeatherInfo(cityName);
        } else {
            Toast.makeText(getContext(), "User City not found...", Toast.LENGTH_SHORT).show();
        }

        loadDevices(view.getContext());
    }

    private void loadDevices(Context context) {
        deviceArrayList = new ArrayList<>();
        deviceAdapter = new DeviceAdapter(context, deviceArrayList);
        binding.deviceRV.setAdapter(deviceAdapter);

        LinearLayout noDeviceFoundV = binding.noDeviceFound;
        TextView deviceCountV = binding.deviceCount;

        this.controller.getDeviceUpdates(response -> {
            deviceArrayList.clear();
            deviceArrayList.addAll(response.subList(0, response.size()));

            deviceCountV.setText(deviceArrayList.size() + " Devices");
            deviceAdapter.notifyDataSetChanged();

            int visibility = deviceArrayList.size() > 0 ? View.INVISIBLE : View.VISIBLE;
            noDeviceFoundV.setVisibility(visibility);
        });
    }

    private void setWeatherInfo(String cityName) {
        Toast.makeText(getContext(), "Your city name is - " + cityName, Toast.LENGTH_LONG).show();

       this.controller.getWeatherInfo(cityName, res -> {
           if(res != null) {
               Toast.makeText(getContext(), "Got back", Toast.LENGTH_LONG).show();
               binding.temp.setText(res.getTemperature() + "°c");
               binding.tempCondition.setText(res.getCondition());
               binding.cityName.setText(res.getCityName());

               Picasso.get().load("https:".concat(res.getIconUrl())).into(binding.tempIcon);

           } else {
               Toast.makeText(getContext(), "No City found for your locality", Toast.LENGTH_SHORT).show();
           }
       });
    }


    private Location getLocation(Context context) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    PERMISSION_CODE
            );
        }


        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Nullable()
    private String getCityName() {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        //Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

        String value = null;
        Geocoder geocoder = new Geocoder(getActivity().getBaseContext(), Locale.getDefault());
        try{
            Log.d("TAG", latitude + " " + longitude);
            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 10);
            for(Address add : addressList) {
                if(add != null) {
                    String locality =  add.getLocality();
                    if(locality != null && !locality.equals("")) {
                        value = locality;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}