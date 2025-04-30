package com.example.weatherapp_mobileadvance.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class WeatherNotificationScheduler {

    private static final String TAG = "WeatherNotificationScheduler";

    /**
     * Lên lịch gửi thông báo hàng ngày vào một giờ/phút cố định (ví dụ: 16h00 mỗi ngày)
     */
    public static void scheduleDailyNotificationAt(Context context, int hourOfDay, int minute) {
        long triggerTime = getNextTriggerTime(hourOfDay, minute);
        Log.d(TAG, "Thời gian thông báo tiếp theo (millis): " + triggerTime);

        Intent intent = new Intent(context, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
            Log.d(TAG, "Thông báo đã được lên lịch hàng ngày vào " + hourOfDay + ":" + minute);
        } else {
            Log.e(TAG, "Không thể lấy AlarmManager.");
        }
    }

    /**
     * Lên lịch gửi thông báo định kỳ sau mỗi intervalMillis (ví dụ mỗi 30 giây)
     */
    public static void scheduleRepeatingNotification(Context context, long intervalMillis) {
        long triggerTime = System.currentTimeMillis();

        Intent intent = new Intent(context, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    intervalMillis,
                    pendingIntent
            );
            Log.d(TAG, "Thông báo định kỳ mỗi " + (intervalMillis / 1000) + " giây đã được lên lịch.");
        } else {
            Log.e(TAG, "Không thể lấy AlarmManager.");
        }
    }

    /**
     * Tính toán thời gian (milliseconds) cho thời điểm gần nhất trong tương lai ứng với giờ/phút chỉ định
     */
    private static long getNextTriggerTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long now = System.currentTimeMillis();
        long scheduledTime = calendar.getTimeInMillis();

        if (scheduledTime <= now) {
            // Nếu thời gian đã trôi qua trong ngày, chuyển sang ngày mai
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            scheduledTime = calendar.getTimeInMillis();
        }

        return scheduledTime;
    }

    /**
     * Hủy thông báo đã lên lịch (nếu cần)
     */
    public static void cancelScheduledNotification(Context context) {
        Intent intent = new Intent(context, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Log.d(TAG, "Đã hủy lịch gửi thông báo.");
        }
    }
}