package com.example.weatherapp_mobileadvance.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp_mobileadvance.R;
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HourlyAdapter extends RecyclerView.Adapter<HourlyAdapter.HourlyViewHolder>{
    private List<HourlyForecast> list;
    private boolean isCelsius = true;

    public HourlyAdapter(List<HourlyForecast> list) {
        this.list = list;
    }

    public void setData(List<HourlyForecast> newData) {
        this.list = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hourly, parent, false);
        return new HourlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
        HourlyForecast item = list.get(position);
        holder.tvHour.setText(item.hour);
        Picasso.get().load(item.iconUrl).into(holder.imgIcon);

        String temp = item.getTemperature(); // VD: "30.24°C"
        Log.d("NHIET", temp);

        String tempText = temp; // Mặc định
        try {
            if (temp.contains("°C")) {
                // Đang °C, chuyển sang °F nếu cần
                float value = Float.parseFloat(temp.replace("°C", ""));
                if (!isCelsius) { // Nếu app đang muốn °F
                    float fValue = (value * 9 / 5) + 32;
                    tempText = String.format("%.1f°F", fValue);
                }
            } else if (temp.contains("°F")) {
                // Đang °F, chuyển sang °C nếu cần
                float value = Float.parseFloat(temp.replace("°F", ""));
                if (isCelsius) { // Nếu app đang muốn °C
                    float cValue = (value - 32) * 5 / 9;
                    tempText = String.format("%.1f°C", cValue);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            // Nếu bị lỗi parse thì giữ nguyên temp
        }

        holder.tvTemp.setText(tempText);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    static class HourlyViewHolder extends RecyclerView.ViewHolder {
        TextView tvHour, tvTemp;
        ImageView imgIcon;

        public HourlyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHour = itemView.findViewById(R.id.tv_hour);
            tvTemp = itemView.findViewById(R.id.tv_temp);
            imgIcon = itemView.findViewById(R.id.img_icon);
        }
    }

    public void toggleTempUnit() {
        isCelsius = !isCelsius;
        notifyDataSetChanged(); // Refresh toàn bộ list
    }
}
