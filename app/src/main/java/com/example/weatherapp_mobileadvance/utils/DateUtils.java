package com.example.weatherapp_mobileadvance.utils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    public static String formatToVietnameseDay(long dt) {
        // Chuyển đổi Unix timestamp (giây) thành đối tượng Date
        Date date = new Date(dt * 1000); // Nhân với 1000 để chuyển đổi từ giây sang mili giây

        // Định dạng ngày giờ theo kiểu Việt Nam
        SimpleDateFormat outputFormat = new SimpleDateFormat("EEEE, dd/MM", new Locale("vi", "VN"));
        String formatted = outputFormat.format(date);

        // Chuyển chữ cái đầu của "thứ" thành hoa (Thứ hai -> Thứ Hai)
        return capitalizeFirstLetter(formatted);
    }

    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
