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

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastAdapter.DailyViewHolder> {

    private List<DailyForecast> forecastList;

    public DailyForecastAdapter(List<DailyForecast> forecastList) {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily, parent, false); // đảm bảo file XML của bạn là item_daily_forecast.xml
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyForecast forecast = forecastList.get(position);
        holder.tvDay.setText(forecast.day);
        holder.imgIcon.setImageResource(forecast.iconResId);
        holder.tvTemp.setText(forecast.temperatureRange);
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvTemp;
        ImageView imgIcon;

        public DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day);
            tvTemp = itemView.findViewById(R.id.tv_temp);
            imgIcon = itemView.findViewById(R.id.img_icon);
        }
    }
}