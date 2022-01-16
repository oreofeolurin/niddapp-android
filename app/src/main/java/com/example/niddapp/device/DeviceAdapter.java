package com.example.niddapp.device;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.niddapp.R;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Device> deviceArrayList;

    public DeviceAdapter(Context context, ArrayList<Device> deviceArrayList) {
        this.context = context;
        this.deviceArrayList = deviceArrayList;
    }
 
    @NonNull
    @Override
    public DeviceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceAdapter.ViewHolder holder, int position) {
        Device model = deviceArrayList.get(position);
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return deviceArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.deviceName);
        }
    }
}
