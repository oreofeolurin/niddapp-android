package com.example.niddapp.weather;

public class WeatherInfo {
    private int temperature;
    private String condition;
    private String cityName;
    private String iconUrl;

    public int getTemperature() {
        return temperature;
    }

    public String getCondition() {
        return condition;
    }

    public String getCityName() {
        return cityName;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public WeatherInfo(int temperature, String condition, String cityName, String iconUrl) {
        this.temperature = temperature;
        this.condition = condition;
        this.cityName = cityName;
        this.iconUrl = iconUrl;
    }
}
