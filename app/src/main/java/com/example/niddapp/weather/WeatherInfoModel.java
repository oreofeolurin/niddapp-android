package com.example.niddapp.weather;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.niddapp.MainController;
import com.example.niddapp.weather.WeatherInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherInfoModel {

    private MainController controller;
    private WeatherInfo weatherInfo;

    public WeatherInfo getWeatherInfo() {
        return weatherInfo;
    }

    public WeatherInfoModel(MainController controller) {
        this.controller = controller;

    }

    public void getData(String cityName, Callback callback) {

        String url = "https://api.weatherapi.com/v1/current.json?key=fe61b1ba18db49ae89430014221201&q=" + cityName + "&aqi=no";

        RequestQueue requestQueue = Volley.newRequestQueue(this.controller.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                (Response.Listener<JSONObject>) res -> {
                    this.onLoadResponse(res);
                    callback.onCallback(weatherInfo);
                },
                (Response.ErrorListener) error -> {
                    error.printStackTrace();
                    callback.onCallback(null);
                });

        requestQueue.add(jsonObjectRequest);
    }

    private void onLoadResponse(JSONObject res) {
        try {

            JSONObject locationJsonObject =  res.getJSONObject("location");
            String cityName = locationJsonObject.getString("name");
            JSONObject current =  res.getJSONObject("current");
            int temperature =  current.getInt("temp_c");
            String iconUrl =  current.getJSONObject("condition").getString("icon");
            String condition =  current.getJSONObject("condition").getString("text");

            weatherInfo = new WeatherInfo(temperature, condition, cityName, iconUrl);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public interface Callback {
        void onCallback(WeatherInfo response);
    }

}
