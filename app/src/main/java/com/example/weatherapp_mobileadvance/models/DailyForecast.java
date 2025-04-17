package com.example.weatherapp_mobileadvance.models;

public class DailyForecast {
    public String day;
    public int iconResId;
    public String temperatureRange;

    public DailyForecast(String day, int iconResId, String temperatureRange) {
        this.day = day;
        this.iconResId = iconResId;
        this.temperatureRange = temperatureRange;
    }
}
