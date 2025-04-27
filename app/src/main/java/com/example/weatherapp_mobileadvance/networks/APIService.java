package com.example.weatherapp_mobileadvance.networks;

import com.example.weatherapp_mobileadvance.models.DailyForecastResponse;
import com.example.weatherapp_mobileadvance.models.ForecastResponse;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("forecast/hourly")
     Call<ForecastResponse> getHourlyForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
    @GET("forecast/daily")
    Call<DailyForecastResponse> getDailyForecast(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("cnt") int cnt,
            @Query("appid") String apiKey
    );
    @GET("weather")
    Call<WeatherResponse> getWeatherByCoordinates(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String units,
            @Query("lang") String lang
    );
}
