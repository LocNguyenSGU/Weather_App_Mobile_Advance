package com.example.weatherapp_mobileadvance.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp_mobileadvance.models.ForecastResponse;  // Thêm import cho ForecastResponse
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.networks.APIClient;
import com.example.weatherapp_mobileadvance.networks.APIService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherResponse> weatherLiveData = new MutableLiveData<>();
    private MutableLiveData<List<HourlyForecast>> hourlyForecastLiveData = new MutableLiveData<>();

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

//    public void fetchHourlyForecast(double lat, double lon) {
//        APIService apiService = APIClient.getProApiService();  // Dùng APIService Pro cho forecast hourly
//        apiService.getHourlyForecast(lat, lon, "8de17430a553b6a80641653b99cd3757", "metric", "vi")
//                .enqueue(new Callback<ForecastResponse>() {
//                    @Override
//                    public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//
//                            // Lấy danh sách forecast từ trường 'list' trong ForecastResponse
//                            List<ForecastResponse.ForecastItem> forecastList = response.body().list;
//                            // Cập nhật LiveData với danh sách forecast
//                            hourlyForecastLiveData.setValue((ForecastResponse) forecastList);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ForecastResponse> call, Throwable t) {
//                        hourlyForecastLiveData.setValue(null);  // Nếu thất bại thì set null
//                    }
//                });
//    }
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
}