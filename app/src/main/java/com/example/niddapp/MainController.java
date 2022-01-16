package com.example.niddapp;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.niddapp.device.Device;
import com.example.niddapp.device.DeviceModel;
import com.example.niddapp.weather.WeatherInfoModel;

public class MainController {
    Fragment view;
    DeviceModel deviceModel;
    WeatherInfoModel weatherInfoModel;

    public MainController(Fragment view) {
        this.view = view;
        deviceModel = new DeviceModel();
        weatherInfoModel =  new WeatherInfoModel(this);
    }

    public void getWeatherInfo(String cityName, WeatherInfoModel.Callback callback) {
        weatherInfoModel.getData(cityName, callback);
    }

    public void getDeviceUpdates(DeviceModel.Callback callback) {
        deviceModel.onDataChange(callback);
    }

    public void saveDevice(String deviceId, String deviceName){
        deviceModel.saveDevice(new Device(deviceId, deviceName));
    }

    public Context getContext() {
        return this.view.getContext();
    }
}
