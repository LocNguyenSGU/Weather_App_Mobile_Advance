package com.example.weatherapp_mobileadvance;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp_mobileadvance.adapter.DailyForecastAdapter;
import com.example.weatherapp_mobileadvance.adapter.HourlyAdapter;
import com.example.weatherapp_mobileadvance.models.DailyForecast;
import com.example.weatherapp_mobileadvance.models.ForecastResponse;
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.networks.APIService;
import com.example.weatherapp_mobileadvance.viewModel.WeatherViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private TextView tvLocation, tvTemperature, tvDescription, tvHumidity, tvWindSpeed;
    private ImageView imgWeatherIcon;
    private WeatherViewModel weatherViewModel;
    private HourlyAdapter hourlyAdapter;
    private RecyclerView recyclerHourly, recyclerDaily;
    private DailyForecastAdapter dailyAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Khởi tạo các View trong giao diện
        tvLocation = findViewById(R.id.tv_location);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvDescription = findViewById(R.id.tv_description);
        tvHumidity = findViewById(R.id.tv_humidity);
        tvWindSpeed = findViewById(R.id.tv_wind_speed);
        imgWeatherIcon = findViewById(R.id.imgWeatherIcon);

        // Khởi tạo WeatherViewModel
        weatherViewModel = new WeatherViewModel();

        // Quan sát dữ liệu thời tiết từ WeatherViewModel
        weatherViewModel.getWeather().observe(this, new Observer<WeatherResponse>() {
            @Override
            public void onChanged(WeatherResponse weatherResponse) {
                if (weatherResponse != null) {
                    // Cập nhật UI khi có dữ liệu thời tiết mới
                    tvLocation.setText(weatherResponse.getName());
                    tvTemperature.setText(weatherResponse.getMain().getTemp() + "°C");
                    tvDescription.setText(weatherResponse.getWeather().get(0).getDescription());
                    tvHumidity.setText("Độ ẩm: " + weatherResponse.getMain().getHumidity() + "%");
                    tvWindSpeed.setText("Gió: " + weatherResponse.getWind().getSpeed() + " m/s");

                    // Lấy icon thời tiết từ API và hiển thị vào ImageView
                    String iconCode = weatherResponse.getWeather().get(0).getIcon();
                    String iconUrl = "https://openweathermap.org/img/wn/" + iconCode + "@2x.png";
                    Picasso.get().load(iconUrl).into(imgWeatherIcon);
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy dữ liệu thời tiết.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lấy thông tin thời tiết từ API (Ví dụ: Hồ Chí Minh)
        weatherViewModel.fetchWeather("Ho Chi Minh");

        // Ánh xạ view
        recyclerHourly = findViewById(R.id.recyclerHourly);
        recyclerHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        hourlyAdapter = new HourlyAdapter(new ArrayList<>());
        recyclerHourly.setAdapter(hourlyAdapter);

        weatherViewModel = new WeatherViewModel();

        // Quan sát forecast
        weatherViewModel.getHourlyForecast().observe(this, new Observer<List<HourlyForecast>>() {
            @Override
            public void onChanged(List<HourlyForecast> forecasts) {
                if (forecasts != null) {
                    hourlyAdapter.setData(forecasts);  // Cập nhật data adapter
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy dữ liệu dự báo giờ.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Lấy dự báo theo giờ từ API (ví dụ: Hồ Chí Minh)
        weatherViewModel.fetchHourlyForecast(10.75, 106.6667);

        // Ánh xạ RecyclerView cho DailyForecast
        recyclerDaily = findViewById(R.id.recyclerDaily);
        recyclerDaily.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter rỗng
        dailyAdapter = new DailyForecastAdapter(new ArrayList<>());
        recyclerDaily.setAdapter(dailyAdapter);

        // Quan sát LiveData từ ViewModel
        weatherViewModel.getDailyForecast().observe(this, new Observer<List<DailyForecast>>() {
            @Override
            public void onChanged(List<DailyForecast> dailyForecasts) {
                if (dailyForecasts != null) {
                    dailyAdapter.setData(dailyForecasts); // Giả sử bạn có hàm này trong Adapter
                } else {
                    Toast.makeText(MainActivity.this, "Không thể lấy dữ liệu dự báo ngày.", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        RecyclerView recyclerDaily = findViewById(R.id.recyclerDaily);
//
//        List<DailyForecast> dailyList = new ArrayList<>();
//        dailyList.add(new DailyForecast("Thứ 4", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 5", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 6", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 7", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 2", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 3", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 4", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 5", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 6", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//        dailyList.add(new DailyForecast("Thứ 47", "Nhiều mây", "https://openweathermap.org/img/wn/04n@2x.png" , "26°C", "32°C"));
//
//
//        // Thêm bao nhiêu ngày tùy bạn
//
//        DailyForecastAdapter adapter = new DailyForecastAdapter(dailyList);
//        recyclerDaily.setLayoutManager(new LinearLayoutManager(this));
//        recyclerDaily.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}