package com.example.weatherapp_mobileadvance.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp_mobileadvance.models.DailyForecast;
import com.example.weatherapp_mobileadvance.models.ForecastResponse;  // Thêm import cho ForecastResponse
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.networks.APIClient;
import com.example.weatherapp_mobileadvance.networks.APIService;
import com.example.weatherapp_mobileadvance.utils.DateUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherResponse> weatherLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HourlyForecast>> hourlyForecastLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DailyForecast>> dailyForecastLiveData = new MutableLiveData<>();

    public LiveData<List<DailyForecast>> getDailyForecast() {
        return dailyForecastLiveData;
    }

    public LiveData<WeatherResponse> getWeather() {
        return weatherLiveData;
    }

    public LiveData<List<HourlyForecast>> getHourlyForecast() {
        return hourlyForecastLiveData;  // Trả về LiveData cho hourly forecast
    }

    public void fetchWeather(String city) {
        APIClient defaultClient = new APIClient("https://api.openweathermap.org/data/2.5/");
        APIService apiService = defaultClient.createService();
        apiService.getCurrentWeather(city, "8de17430a553b6a80641653b99cd3757", "metric", "vi")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            weatherLiveData.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherResponse> call, Throwable t) {
                        weatherLiveData.setValue(null);
                    }
                });
    }

    public void fetchHourlyForecast(double lat, double lon) {
        APIClient proClient = new APIClient("https://pro.openweathermap.org/data/2.5/");
        APIService apiService = proClient.createService();
        apiService.getHourlyForecast(lat, lon, "8de17430a553b6a80641653b99cd3757", "metric", "vi")
            .enqueue(new Callback<ForecastResponse>() {
                @Override
                public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<HourlyForecast> hourlyForecastList = new ArrayList<>();
                        for (ForecastResponse.ForecastItem item : response.body().getList()) {
                            String hour = item.getDt_txt().split(" ")[1].substring(0, 5); // Giờ
                            String temperature = item.getMain().getTemp() + "°C";

                            // Lấy icon URL từ mã icon
                            String iconCode = item.getWeather().get(0).getIcon();
                            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                            Log.d("IconURL", iconUrl);
                            // Vì HourlyForecast yêu cầu int iconResId, ta sửa lại class để dùng String iconUrl
                            hourlyForecastList.add(new HourlyForecast(hour, iconUrl, temperature));
                        }
                        // Gán forecast theo ngày
                        List<DailyForecast> dailyList = convertToDailyForecast(response.body().getList());
                        dailyForecastLiveData.setValue(dailyList);
                        hourlyForecastLiveData.setValue(hourlyForecastList);
                    } else {
                        hourlyForecastLiveData.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<ForecastResponse> call, Throwable t) {
                    hourlyForecastLiveData.setValue(null);
                }
            });
    }

    private List<DailyForecast> convertToDailyForecast(List<ForecastResponse.ForecastItem> forecastList) {
        Map<String, List<ForecastResponse.ForecastItem>> dailyMap = new LinkedHashMap<>();

        for (ForecastResponse.ForecastItem item : forecastList) {
            String date = item.getDt_txt().split(" ")[0]; // ví dụ: 2025-04-17
            dailyMap.computeIfAbsent(date, k -> new ArrayList<>()).add(item);
        }

        List<DailyForecast> dailyForecasts = new ArrayList<>();

        for (Map.Entry<String, List<ForecastResponse.ForecastItem>> entry : dailyMap.entrySet()) {
            String date = entry.getKey();
            List<ForecastResponse.ForecastItem> items = entry.getValue();

            double minTemp = Double.MAX_VALUE;
            double maxTemp = Double.MIN_VALUE;
            ForecastResponse.ForecastItem middleItem = items.get(items.size() / 2);

            for (ForecastResponse.ForecastItem item : items) {
                double temp = item.getMain().getTemp();
                if (temp < minTemp) minTemp = temp;
                if (temp > maxTemp) maxTemp = temp;
            }
            String dayFormatted = DateUtils.formatToVietnameseDay(middleItem.getDt_txt());
            String iconCode = middleItem.getWeather().get(0).getIcon();
            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
//            String description = middleItem.getWeather().get(0).getDescription();

            dailyForecasts.add(new DailyForecast(
                    dayFormatted,
                    iconUrl,
                    String.format("%.1f°C", minTemp),
                    String.format("%.1f°C", maxTemp)
            ));
        }

        return dailyForecasts;
    }
}