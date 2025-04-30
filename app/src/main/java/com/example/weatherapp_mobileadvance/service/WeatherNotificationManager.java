package com.example.weatherapp_mobileadvance.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.weatherapp_mobileadvance.R;

public class WeatherNotificationManager {
    private static final String TAG = "WeatherNotificationManager";
    private static final float TEMP_THRESHOLD = 29.0f; // Ngưỡng nhiệt độ (°C)
    private static final int HUMIDITY_THRESHOLD = 20; // Ngưỡng độ ẩm (%)
    private static final String BAD_WEATHER_CONDITION = "Rain"; // Thời tiết xấu
    private static final String CHANNEL_ID = "weather_channel"; // ID kênh thông báo

    private static long lastNotifiedTime = 0;

    public static void checkAndNotifyWeather(Context context, float temp, int humidity, String weatherDescription) {
        Log.d(TAG, "Checking weather...");

        long currentTime = System.currentTimeMillis();
        long timeSinceLastNotification = currentTime - lastNotifiedTime;

        if (temp > TEMP_THRESHOLD || humidity > HUMIDITY_THRESHOLD || weatherDescription.contains(BAD_WEATHER_CONDITION)) {
            Log.d(TAG, "Thỏa mãn điều kiện gửi thông báo.");

            if (timeSinceLastNotification >= 60 * 1000) { // ít nhất 1 phút
                sendWeatherAlert(context, temp, humidity, weatherDescription);
                lastNotifiedTime = currentTime;
            } else {
                Log.d(TAG, "Thông báo đã được gửi gần đây. Đợi thêm " + (60 - timeSinceLastNotification / 1000) + " giây.");
            }
        } else {
            Log.d(TAG, "Không thỏa mãn điều kiện cảnh báo.");
        }
    }

    private static void sendWeatherAlert(Context context, float temp, int humidity, String weatherDescription) {
        String message = "Cảnh báo thời tiết:\n";
        message += "Nhiệt độ: " + temp + "°C\n";
        message += "Độ ẩm: " + humidity + "%\n";
        message += "Mô tả: " + weatherDescription;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_alert)
                .setContentTitle("Cảnh báo thời tiết")
                .setContentText("Nhấn để xem chi tiết")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo channel nếu cần
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Weather Alerts";
            String description = "Channel for weather alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());

        Log.d(TAG, "Thông báo đã được gửi.");
    }
}