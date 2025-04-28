package com.example.weatherapp_mobileadvance.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.weatherapp_mobileadvance.R;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;

public class WeatherNotificationManager {

    private static final String TAG = "WeatherNotificationScheduler";
    private static final float TEMP_THRESHOLD = 29.0f; // Ngưỡng nhiệt độ (°C)
    private static final int HUMIDITY_THRESHOLD = 20; // Ngưỡng độ ẩm (%)
    private static final String BAD_WEATHER_CONDITION = "Rain"; // Thời tiết xấu
    private static final String CHANNEL_ID = "weather_channel"; // ID kênh thông báo

    // Kiểm tra điều kiện và gửi thông báo nếu cần
    public static void checkAndNotifyWeather(Context context, WeatherResponse weatherResponse) {
        if (weatherResponse != null) {
            float temp = weatherResponse.getMain().getTemp();
            int humidity = weatherResponse.getMain().getHumidity();
            String weatherDescription = weatherResponse.getWeather().get(0).getDescription();

            // Log thông tin thời tiết
            Log.d(TAG, "Nhiệt độ: " + temp + "°C");
            Log.d(TAG, "Độ ẩm: " + humidity + "%");
            Log.d(TAG, "Mô tả thời tiết: " + weatherDescription);

            // Kiểm tra điều kiện thông báo
            if (temp > TEMP_THRESHOLD || humidity > HUMIDITY_THRESHOLD || weatherDescription.contains(BAD_WEATHER_CONDITION)) {
                Log.d(TAG, "Điều kiện thông báo thỏa mãn.");
                sendWeatherAlert(context, temp, humidity, weatherDescription);
            } else {
                Log.d(TAG, "Điều kiện thông báo không thỏa mãn.");
            }
        } else {
            Log.d(TAG, "Dữ liệu thời tiết không hợp lệ.");
        }
    }

    private static void sendWeatherAlert(Context context, float temp, int humidity, String weatherDescription) {
        // Log các giá trị truyền vào
        Log.d("WeatherAlert", "Nhiệt độ: " + temp + "°C");
        Log.d("WeatherAlert", "Độ ẩm: " + humidity + "%");
        Log.d("WeatherAlert", "Mô tả thời tiết: " + weatherDescription);

        String message = "Cảnh báo thời tiết:\n";
        message += "Nhiệt độ: " + temp + "°C\n";
        message += "Độ ẩm: " + humidity + "%\n";
        message += "Mô tả: " + weatherDescription;

        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_alert) // Biểu tượng thông báo
                .setContentTitle("Cảnh báo thời tiết")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true); // Tự động hủy thông báo khi người dùng nhấn vào

        // Log thông báo trực tiếp
        Log.d("WeatherNotificationScheduler", "Thông báo sẽ được tạo với tiêu đề: Cảnh báo thời tiết và nội dung: " + message);

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo kênh thông báo cho Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Weather Alerts";
            String description = "Channel for weather alerts";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);

            // Log thông tin kênh thông báo
            Log.d("WeatherNotificationScheduler", "Kênh thông báo được tạo với ID: " + CHANNEL_ID);
        }

        // Gửi thông báo
        notificationManager.notify(1, builder.build());

        // Log thông tin gửi thông báo
        Log.d("WeatherNotificationScheduler", "Thông báo đã được gửi.");
    }
}