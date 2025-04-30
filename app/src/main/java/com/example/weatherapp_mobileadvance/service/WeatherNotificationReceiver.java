package com.example.weatherapp_mobileadvance.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.weatherapp_mobileadvance.R;

public class WeatherNotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "weather_channel";
    private static final int NOTIFICATION_ID = 1001;
    private static final String TAG = "WeatherNotificationReceiver";

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Đã nhận được broadcast từ AlarmManager. Chuẩn bị hiển thị thông báo...");

        // Tạo kênh thông báo (cho Android 8+)
        createNotificationChannel(context);

        // Tạo nội dung thông báo (bạn có thể cập nhật thông tin thời tiết thật tại đây)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_weather_alert) // Đảm bảo bạn có icon trong drawable
                .setContentTitle("Dự báo thời tiết")
                .setContentText("Trời có mây nhẹ và không có mưa hôm nay.") // Có thể thay bằng dữ liệu API thật
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        Log.d(TAG, "Thông báo thời tiết đã được hiển thị.");
    }

    // Tạo notification channel (bắt buộc từ Android 8.0 trở lên)
    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Kênh Thời Tiết";
            String description = "Kênh dùng để hiển thị thông báo thời tiết hàng ngày";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký kênh với hệ thống
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Log.d(TAG, "Notification channel đã được tạo.");
            }
        }
    }
}