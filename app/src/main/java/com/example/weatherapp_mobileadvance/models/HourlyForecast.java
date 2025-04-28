package com.example.weatherapp_mobileadvance.models;

public class HourlyForecast {
    public String hour;
    public String iconUrl;
    public String temperature;

    public HourlyForecast(String hour, String iconUrl, String temperature) {
        this.hour = hour;
        this.iconUrl = iconUrl;
        this.temperature = temperature;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
}
