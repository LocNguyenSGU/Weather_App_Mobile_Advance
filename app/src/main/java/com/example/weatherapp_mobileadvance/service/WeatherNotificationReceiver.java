package com.example.weatherapp_mobileadvance.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.weatherapp_mobileadvance.models.WeatherResponse;

public class WeatherNotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Lấy dữ liệu thời tiết và kiểm tra để gửi thông báo
        Log.d("WeatherNotificationScheduler", "Nhận tín hiệu từ AlarmManager, gửi thông báo...");

        // Giả lập dữ liệu WeatherResponse, trong thực tế bạn sẽ lấy từ API hoặc dữ liệu đã có
//        WeatherResponse weatherResponse = new WeatherResponse(); // Thay bằng đối tượng thật
//        WeatherNotificationManager.checkAndNotifyWeather(context, weatherResponse);
    }
}