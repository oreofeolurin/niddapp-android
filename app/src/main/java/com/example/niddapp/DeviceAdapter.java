package com.example.niddapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DeviceModel> deviceModelArrayList;

    public DeviceAdapter(Context context, ArrayList<DeviceModel> deviceModelArrayList) {
        this.context = context;
        this.deviceModelArrayList = deviceModelArrayList;
    }
 
    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        DeviceModel model = deviceModelArrayList.get(position);
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return deviceModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.deviceName);
        }
    }
}
