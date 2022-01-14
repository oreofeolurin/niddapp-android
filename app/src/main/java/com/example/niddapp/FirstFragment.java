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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.niddapp.databinding.FragmentFirstBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FirstFragment extends Fragment {
    private DatabaseReference devicesCollection;
    private ArrayList<DeviceModel> deviceModelArrayList;
    private DeviceAdapter deviceAdapter;

    private LocationManager locationManager;
    private Location location;
    private int PERMISSION_CODE = 1;
    private String cityName;

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initiateFirebaseDB();

        deviceModelArrayList = new ArrayList<>();
        binding.deviceCount.setText(String.valueOf(deviceModelArrayList.size()) + " Devices");
        deviceAdapter = new DeviceAdapter(view.getContext(), deviceModelArrayList);
        binding.deviceRV.setAdapter(deviceAdapter);


        location = getLocation(view.getContext());
        cityName = getCityName();

        if(cityName != null) {
            getWeatherInfo(cityName);
        } else {
            Toast.makeText(getContext(), "User City not found...", Toast.LENGTH_SHORT).show();
        }
    }

    private void initiateFirebaseDB() {
        devicesCollection = FirebaseDatabase.getInstance().getReference().child("NiddApp").child("devices");

        LinearLayout noDeviceFoundV = binding.noDeviceFound;
        TextView deviceCountV = binding.deviceCount;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                deviceModelArrayList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    deviceModelArrayList.add(snapshot.getValue(DeviceModel.class));
                }

                deviceCountV.setText(deviceModelArrayList.size() + " Devices");
                deviceAdapter.notifyDataSetChanged();

                int visibility = deviceModelArrayList.size() > 0 ? View.INVISIBLE : View.VISIBLE;
                noDeviceFoundV.setVisibility(visibility);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };
        devicesCollection.addValueEventListener(postListener);
    }


    private void getWeatherInfo(String cityName) {
        Toast.makeText(getContext(), "Your city name is - " + cityName, Toast.LENGTH_LONG).show();

        String url = "https://api.weatherapi.com/v1/current.json?key=fe61b1ba18db49ae89430014221201&q=" + cityName + "&aqi=no";

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) res -> {
                    Toast.makeText(getContext(), "Got back", Toast.LENGTH_LONG).show();
                    onLoadResponse(res);
                },
                (Response.ErrorListener) error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "No City found for your locality", Toast.LENGTH_SHORT).show();
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void onLoadResponse(JSONObject res) {
        binding.loading.setVisibility(View.GONE);
        binding.container.setVisibility(View.VISIBLE);

        try {

            JSONObject locationJsonObject =  res.getJSONObject("location");
            String name = locationJsonObject.getString("name");
            // String country = locationJsonObject.getString("country");

            JSONObject current =  res.getJSONObject("current");
            String temperature =  current.getString("temp_c");
            int isDay =  current.getInt("is_day");
            String iconUrl =  current.getJSONObject("condition").getString("icon");
            String condition =  current.getJSONObject("condition").getString("text");

            binding.temp.setText(temperature + "°c");
            binding.tempCondition.setText(condition);
            // binding.cityName.setText(name + ", "+ country);
            binding.cityName.setText(name);

            Picasso.get().load("https:".concat(iconUrl)).into(binding.tempIcon);

           /* if(isDay == 1) {
                Picasso.get().load(getString(R.string.day_bg)).into(weatherBgV);
            } else {
                Log.d("TAG", getString(R.string.night_bg));
                Picasso.get().load(getString(R.string.night_bg)).into(weatherBgV);
            }*/

        } catch (JSONException e) {
            e.printStackTrace();
        }
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