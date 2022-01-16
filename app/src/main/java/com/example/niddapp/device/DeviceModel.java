package com.example.niddapp.device;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DeviceModel {
    private DatabaseReference devicesCollection;
    private ArrayList<Device> deviceArrayList = new ArrayList<Device>();
    private String name;
    private String id;


    public ArrayList<Device> getDevices() {
        return deviceArrayList;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public DeviceModel() {
        devicesCollection = FirebaseDatabase.getInstance().getReference().child("NiddApp").child("devices");
    }


    public void onDataChange(Callback callback) {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deviceArrayList.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    deviceArrayList.add(snapshot.getValue(Device.class));
                }

                callback.onCallback(deviceArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("TAG", "loadPost:onCancelled", databaseError.toException());
            }
        };

        devicesCollection.addValueEventListener(postListener);
    }

    public void saveDevice(Device device) {
        HashMap<String, Object> map  = new HashMap<>();
        map.put(device.getId(), device);

        devicesCollection.updateChildren(map);
    }

    public interface Callback {
        void onCallback(ArrayList<Device> response);
    }
}
