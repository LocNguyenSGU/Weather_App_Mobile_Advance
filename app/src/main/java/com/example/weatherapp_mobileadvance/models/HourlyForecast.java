package com.example.weatherapp_mobileadvance.models;

public class HourlyForecast {
    public String hour;
    public int iconResId;
    public String temperature;

    public HourlyForecast(String hour, int iconResId, String temperature) {
        this.hour = hour;
        this.iconResId = iconResId;
        this.temperature = temperature;
    }
}
