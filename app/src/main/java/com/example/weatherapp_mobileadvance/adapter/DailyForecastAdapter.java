package com.example.weatherapp_mobileadvance.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp_mobileadvance.R;
import com.example.weatherapp_mobileadvance.models.DailyForecast;
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder> {

    private List<DailyForecast> forecastList = new ArrayList<>();
    private boolean isCelsius = true;

    public DailyForecastAdapter(List<DailyForecast> forecastList) {
        this.forecastList = forecastList;
    }

    public void setData(List<DailyForecast> newData) {
        this.forecastList = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false); // Đảm bảo file XML đúng tên
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyForecast forecast = forecastList.get(position);

        holder.tvDate.setText(forecast.date); // Ví dụ: 2025-04-17
//        holder.tvDescription.setText(forecast.description);
        // Load icon từ URL
        Picasso.get()
                .load(forecast.iconUrl)
                .placeholder(R.drawable.ic_placeholder) // Icon tạm trong khi chờ
                .error(R.drawable.ic_error) // Icon lỗi nếu load fail
                .into(holder.imgIcon);

        // Định dạng nhiệt độ:
        String tempRange = forecast.tempMin + " - " + forecast.tempMax; // ví dụ "30°C - 34°C"

        // Gọi hàm để chuyển đổi nhiệt độ (tuỳ isCelsius)
        holder.tvTempRange.setText(formatTemperatureRange(tempRange));

    }

    @Override
    public int getItemCount() {
        if (forecastList == null) {
            return 0; // Nếu danh sách là null, trả về 0
        }
        return forecastList.size();
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTempRange, tvDescription;
        ImageView imgIcon;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDescription = itemView.findViewById(R.id.tv_description);
            imgIcon = itemView.findViewById(R.id.img_weather_icon);
            tvTempRange = itemView.findViewById(R.id.tv_temp_range);
        }
    }

    private String formatTemperatureRange(String tempRange) {
        // tempRange ví dụ: "30°C - 34°C"
        String[] parts = tempRange.split(" - ");
        if (parts.length != 2) return tempRange; // Nếu format sai thì trả luôn

        String tempMin = parts[0].replace("°C", "").replace("°F", "").trim();
        String tempMax = parts[1].replace("°C", "").replace("°F", "").trim();

        try {
            float min = Float.parseFloat(tempMin);
            float max = Float.parseFloat(tempMax);

            if (!isCelsius) {
                // Đổi sang độ F
                min = (min * 9 / 5) + 32;
                max = (max * 9 / 5) + 32;
                return String.format("%.1f°F - %.1f°F", min, max);
            } else {
                // Để độ C
                return String.format("%.1f°C - %.1f°C", min, max);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return tempRange; // lỗi thì trả về như cũ
        }
    }

    public void toggleTempUnit() {
        isCelsius = !isCelsius;
        notifyDataSetChanged(); // Refresh toàn bộ list
    }
}