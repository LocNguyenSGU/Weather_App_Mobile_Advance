package com.example.weatherapp_mobileadvance.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp_mobileadvance.models.DailyForecast;
import com.example.weatherapp_mobileadvance.models.DailyForecastResponse;
import com.example.weatherapp_mobileadvance.models.ForecastResponse;  // Thêm import cho ForecastResponse
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.networks.APIClient;
import com.example.weatherapp_mobileadvance.networks.APIService;
import com.example.weatherapp_mobileadvance.utils.DateUtils;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherResponse> weatherLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HourlyForecast>> hourlyForecastLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DailyForecast>> dailyForecastLiveData = new MutableLiveData<>();

    public LiveData<List<DailyForecast>> getDailyForecast() {
        return dailyForecastLiveData;  // Trả về LiveData cho daily forecast
    }

    public LiveData<WeatherResponse> getWeather() {
        return weatherLiveData;
    }

    public LiveData<List<HourlyForecast>> getHourlyForecast() {
        return hourlyForecastLiveData;  // Trả về LiveData cho hourly forecast
    }

    public void fetchWeatherByCoordinates(double lat, double lon) {
        APIClient defaultClient = new APIClient("https://api.openweathermap.org/data/2.5/");
        APIService apiService = defaultClient.createService();
        apiService.getWeatherByCoordinates(lat, lon, "8de17430a553b6a80641653b99cd3757", "metric", "vi")
                .enqueue(new Callback<WeatherResponse>() {
                    @Override
                    public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Log.d("DEBUG_WEATHER", "Response JSON: " + new Gson().toJson(response.body()));
                            weatherLiveData.setValue(response.body());
                        } else {
                            Log.e("DEBUG_WEATHER", "Không thành công - Code: " + response.code());
                            weatherLiveData.setValue(null);
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
                            try {
                                // Lấy thời gian từ dt_txt và chuyển thành Date
                                String dateTime = item.getDt_txt();
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                inputFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC
                                Date date = inputFormat.parse(dateTime);

                                // Cộng thêm 7 giờ để chuyển sang múi giờ Việt Nam (GMT+7)
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
//                                calendar.add(Calendar.HOUR_OF_DAY, 7); // Thêm 7 giờ

                                // Định dạng lại thời gian sau khi cộng thêm 7 giờ
                                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                                outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Đặt múi giờ Việt Nam
                                String hour = outputFormat.format(calendar.getTime());

                                // Lấy nhiệt độ và icon
                                String temperature = item.getMain().getTemp() + "°C";
                                String iconCode = item.getWeather().get(0).getIcon();
                                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                                Log.d("IconURL", iconUrl);

                                // Thêm vào danh sách dự báo
                                hourlyForecastList.add(new HourlyForecast(hour, iconUrl, temperature));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        // Cập nhật LiveData với danh sách dự báo
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

    public void fetchDailyForecast(double lat, double lon, int numberDay) {
        APIClient proClient = new APIClient("https://api.openweathermap.org/data/2.5/");
        APIService apiService = proClient.createService();

        apiService.getDailyForecast(lat, lon, numberDay, "92738b07716c523fb086173c2d36db32")
                .enqueue(new Callback<DailyForecastResponse>() {
                    @Override
                    public void onResponse(Call<DailyForecastResponse> call, Response<DailyForecastResponse> response) {
                        // Log thông tin về phản hồi
                        Log.d("API Response", "Response code: " + response.code());
                        Log.d("API Response", "Response body: " + response.body());

                        if (response.isSuccessful() && response.body() != null) {
                            List<DailyForecast> dailyForecastList = new ArrayList<>();

                            for (DailyForecastResponse.ForecastItem item : response.body().getList()) {
                                // Log thông tin từng mục trong danh sách dự báo
                                Log.d("ForecastItem", "Date: " + item.getDt() + ", Min Temp: " + item.getTemp().getMin() + ", Max Temp: " + item.getTemp().getMax());

                                // Chuyển từ Kelvin sang Celsius
                                double minTempCelsius = item.getTemp().getMin() - 273.15;
                                double maxTempCelsius = item.getTemp().getMax() - 273.15;

                                // Định dạng nhiệt độ thành chuỗi với dấu "°C"
                                String minTemp = String.format("%.1f°C", minTempCelsius);
                                String maxTemp = String.format("%.1f°C", maxTempCelsius);

                                String iconCode = item.getWeather().get(0).getIcon();
                                String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                                Log.d("IconURL", iconUrl);

                                // Chuyển đổi ngày sang định dạng ngày Việt Nam
                                String dayFormatted = DateUtils.formatToVietnameseDay(item.getDt());

                                // Thêm thông tin dự báo vào danh sách
                                dailyForecastList.add(new DailyForecast(
                                        dayFormatted,      // Ngày dự báo
                                        iconUrl,           // URL của icon
                                        minTemp,           // Nhiệt độ thấp nhất
                                        maxTemp            // Nhiệt độ cao nhất
                                ));
                            }

                            // Log trạng thái của dailyForecastList trước khi cập nhật LiveData
                            Log.d("DailyForecastList", "List size: " + dailyForecastList.size());

                            // Cập nhật LiveData chỉ khi danh sách không null và có dữ liệu
                            if (dailyForecastList != null && !dailyForecastList.isEmpty()) {
                                Log.d("LiveData", "Updating LiveData with daily forecast");
                                dailyForecastLiveData.setValue(dailyForecastList);
                            } else {
                                Log.d("LiveData", "No data to update");
                                dailyForecastLiveData.setValue(null); // Gán giá trị null nếu không có dữ liệu
                            }
                        } else {
                            Log.d("API Response", "Response is not successful or body is null");
                            dailyForecastLiveData.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<DailyForecastResponse> call, Throwable t) {
                        // Xử lý lỗi và ghi log nếu cần
                        Log.e("API Error", t.getMessage(), t);
                        dailyForecastLiveData.setValue(null);
                    }
                });
    }

//    private List<DailyForecast> convertToDailyForecast(List<ForecastResponse.ForecastItem> forecastList) {
//        Map<String, List<ForecastResponse.ForecastItem>> dailyMap = new LinkedHashMap<>();
//
//        for (ForecastResponse.ForecastItem item : forecastList) {
//            String date = item.getDt_txt().split(" ")[0]; // ví dụ: 2025-04-17
//            dailyMap.computeIfAbsent(date, k -> new ArrayList<>()).add(item);
//        }
//
//        List<DailyForecast> dailyForecasts = new ArrayList<>();
//
//        for (Map.Entry<String, List<ForecastResponse.ForecastItem>> entry : dailyMap.entrySet()) {
//            String date = entry.getKey();
//            List<ForecastResponse.ForecastItem> items = entry.getValue();
//
//            double minTemp = Double.MAX_VALUE;
//            double maxTemp = Double.MIN_VALUE;
//            ForecastResponse.ForecastItem middleItem = items.get(items.size() / 2);
//
//            for (ForecastResponse.ForecastItem item : items) {
//                double temp = item.getMain().getTemp();
//                if (temp < minTemp) minTemp = temp;
//                if (temp > maxTemp) maxTemp = temp;
//            }
//            String dayFormatted = DateUtils.formatToVietnameseDay(middleItem.getDt_txt());
//            String iconCode = middleItem.getWeather().get(0).getIcon();
//            String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
////            String description = middleItem.getWeather().get(0).getDescription();
//
//            dailyForecasts.add(new DailyForecast(
//                    dayFormatted,
//                    iconUrl,
//                    String.format("%.1f°C", minTemp),
//                    String.format("%.1f°C", maxTemp)
//            ));
//        }
//
//        return dailyForecasts;
//    }
}