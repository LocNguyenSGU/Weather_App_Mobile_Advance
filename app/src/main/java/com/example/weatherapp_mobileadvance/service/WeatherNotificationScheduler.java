package com.example.weatherapp_mobileadvance.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

public class WeatherNotificationScheduler {

    private static final String TAG = "WeatherNotificationScheduler"; // Đặt tag cho log

    public static void scheduleWeatherNotification(Context context) {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        Log.d(TAG, "Thời gian hiện tại (milliseconds): " + currentTime);

        // Tính toán thời gian còn lại cho 9:36 sáng
        long nineThirtySixAMTime = getNineThirtySixAMTime(currentTime);
        Log.d(TAG, "Thời gian tính cho 9:36 sáng: " + nineThirtySixAMTime);

        // Tạo Intent và PendingIntent
        Intent intent = new Intent(context, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Log.d(TAG, "PendingIntent đã được tạo.");

        // Sử dụng AlarmManager để đặt lịch gửi thông báo
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d(TAG, "Đặt lịch gửi thông báo.");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime, 30 * 1000, pendingIntent);
//            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, nineThirtySixAMTime, AlarmManager.INTERVAL_DAY, pendingIntent);
            Log.d(TAG, "Thông báo đã được lên lịch để gửi vào lúc 9:36 sáng hàng ngày.");
        } else {
            Log.e(TAG, "Không thể lấy AlarmManager.");
        }
    }

    // Hàm tính thời gian cho 9:36 sáng
    private static long getNineThirtySixAMTime(long currentTime) {
        // Lấy thời gian bắt đầu ngày hôm nay (00:00 sáng) theo múi giờ của hệ thống
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);

        // Đặt lại thời gian để bắt đầu từ 00:00 sáng của ngày hôm nay
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Cộng thêm 9 giờ và 36 phút
        calendar.add(Calendar.HOUR_OF_DAY, 10);
        calendar.add(Calendar.MINUTE, 35);

        // Lấy thời gian đã tính toán (mili giây)
        long nineThirtySixAMTime = calendar.getTimeInMillis();

        // Log thời gian đã tính, cần điều chỉnh múi giờ nếu cần
        calendar.setTimeInMillis(nineThirtySixAMTime);
        TimeZone timeZone = TimeZone.getDefault(); // Múi giờ hiện tại của hệ thống
        int offset = timeZone.getOffset(calendar.getTimeInMillis()) / (60 * 1000); // Múi giờ theo phút

        Log.d(TAG, "Thời gian 9:36 sáng (múi giờ hệ thống): " + calendar.getTime().toString() + " (offset: " + offset + " phút)");

        return nineThirtySixAMTime;
    }

    // Hàm để lên lịch thông báo mỗi 30 giây
    public static void scheduleWeatherNotificationEvery30Sec(Context context) {
        // Lấy thời gian hiện tại
        long currentTime = System.currentTimeMillis();
        Log.d(TAG, "Thời gian hiện tại (milliseconds): " + currentTime);

        // Tạo Intent và PendingIntent
        Intent intent = new Intent(context, WeatherNotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Log.d(TAG, "PendingIntent đã được tạo.");

        // Sử dụng AlarmManager để đặt lịch gửi thông báo mỗi 30 giây
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            Log.d(TAG, "Đặt lịch gửi thông báo sau mỗi 30 giây.");
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, currentTime, 30 * 1000, pendingIntent); // 30 * 1000 = 30 giây
            Log.d(TAG, "Thông báo sẽ được gửi mỗi 30 giây.");
        } else {
            Log.e(TAG, "Không thể lấy AlarmManager.");
        }
    }
}