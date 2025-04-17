package com.example.weatherapp_mobileadvance.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.networks.APIClient;
import com.example.weatherapp_mobileadvance.networks.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherViewModel extends ViewModel {
    private final MutableLiveData<WeatherResponse> weatherLiveData = new MutableLiveData<>();

    public LiveData<WeatherResponse> getWeather() {
        return weatherLiveData;
    }

    public void fetchWeather(String city) {
        APIService apiService = APIClient.getApiService();
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
}