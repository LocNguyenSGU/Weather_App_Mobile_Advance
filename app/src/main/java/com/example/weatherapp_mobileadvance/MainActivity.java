package com.example.weatherapp_mobileadvance;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp_mobileadvance.adapter.DailyForecastAdapter;
import com.example.weatherapp_mobileadvance.adapter.HourlyAdapter;
import com.example.weatherapp_mobileadvance.models.DailyForecast;
import com.example.weatherapp_mobileadvance.models.HourlyForecast;
import com.example.weatherapp_mobileadvance.models.WeatherResponse;
import com.example.weatherapp_mobileadvance.viewModel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    private TextView tvLocation, tvTemperature, tvDescription, tvHumidity, tvWindSpeed;
    private ImageView imgWeatherIcon;
    private WeatherViewModel weatherViewModel;
    private HourlyAdapter hourlyAdapter;
    private RecyclerView recyclerHourly, recyclerDaily;
    private DailyForecastAdapter dailyAdapter;
    private FusedLocationProviderClient fusedLocationClient;


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

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1001); // Request code tùy bạn đặt
            return;
        }



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
//        weatherViewModel.fetchWeather("Ho Chi Minh");
        getCurrentLocation();


        // Ánh xạ view
        recyclerHourly = findViewById(R.id.recyclerHourly);
        recyclerHourly.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        hourlyAdapter = new HourlyAdapter(new ArrayList<>());
        recyclerHourly.setAdapter(hourlyAdapter);


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
//        weatherViewModel.fetchHourlyForecast(10.75, 106.6667);

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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Log.d("DEBUGLOC", "Lat11111: " + latitude + " - Lon: " + longitude);
                            // Gọi API với tọa độ lấy được
                             latitude = 10.7769; // hcm city
                             longitude = 106.7009;
                            weatherViewModel.fetchWeatherByCoordinates(latitude, longitude);
                            weatherViewModel.fetchHourlyForecast(latitude, longitude);
                        } else {
                            Toast.makeText(MainActivity.this, "Không lấy được vị trí.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ⬇️ THÊM NGAY SAU onCreate hoặc getCurrentLocation
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation(); // gọi lại sau khi được cấp quyền
        }
    }
}

