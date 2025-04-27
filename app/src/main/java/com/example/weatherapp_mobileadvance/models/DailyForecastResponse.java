package com.example.weatherapp_mobileadvance.models;

import java.util.List;

public class DailyForecastResponse {

    // Danh sách dự báo thời tiết theo giờ (cách nhau 3 giờ)
    public List<DailyForecastResponse.ForecastItem> list;

    public List<DailyForecastResponse.ForecastItem> getList() {
        return list;
    }

    public void setList(List<DailyForecastResponse.ForecastItem> list) {
        this.list = list;
    }

    public static class ForecastItem {

        private long dt;           // Thời gian của dự báo (Unix timestamp)
        private long sunrise;      // Thời gian mặt trời mọc (Unix timestamp)
        private long sunset;       // Thời gian mặt trời lặn (Unix timestamp)
        private Temperature temp;  // Nhiệt độ trong ngày
        private FeelsLike feels_like; // Nhiệt độ cảm nhận
        private int pressure;      // Áp suất
        private int humidity;      // Độ ẩm
        private List<Weather> weather; // Thông tin về thời tiết
        private float speed;       // Tốc độ gió
        private int deg;           // Hướng gió
        private float gust;        // Gió giật
        private int clouds;        // Mây
        private float pop;         // Xác suất mưa
        private float rain;        // Lượng mưa

        // Getter and Setter
        public long getDt() {
            return dt;
        }

        public void setDt(long dt) {
            this.dt = dt;
        }

        public long getSunrise() {
            return sunrise;
        }

        public void setSunrise(long sunrise) {
            this.sunrise = sunrise;
        }

        public long getSunset() {
            return sunset;
        }

        public void setSunset(long sunset) {
            this.sunset = sunset;
        }

        public Temperature getTemp() {
            return temp;
        }

        public void setTemp(Temperature temp) {
            this.temp = temp;
        }

        public FeelsLike getFeels_like() {
            return feels_like;
        }

        public void setFeels_like(FeelsLike feels_like) {
            this.feels_like = feels_like;
        }

        public int getPressure() {
            return pressure;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public List<Weather> getWeather() {
            return weather;
        }

        public void setWeather(List<Weather> weather) {
            this.weather = weather;
        }

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public int getDeg() {
            return deg;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }

        public float getGust() {
            return gust;
        }

        public void setGust(float gust) {
            this.gust = gust;
        }

        public int getClouds() {
            return clouds;
        }

        public void setClouds(int clouds) {
            this.clouds = clouds;
        }

        public float getPop() {
            return pop;
        }

        public void setPop(float pop) {
            this.pop = pop;
        }

        public float getRain() {
            return rain;
        }

        public void setRain(float rain) {
            this.rain = rain;
        }

        // Các lớp con cho thông tin về nhiệt độ, cảm giác, và thời tiết
        public static class Temperature {
            private float day;
            private float min;
            private float max;
            private float night;
            private float eve;
            private float morn;

            // Getter and Setter
            public float getDay() {
                return day;
            }

            public void setDay(float day) {
                this.day = day;
            }

            public float getMin() {
                return min;
            }

            public void setMin(float min) {
                this.min = min;
            }

            public float getMax() {
                return max;
            }

            public void setMax(float max) {
                this.max = max;
            }

            public float getNight() {
                return night;
            }

            public void setNight(float night) {
                this.night = night;
            }

            public float getEve() {
                return eve;
            }

            public void setEve(float eve) {
                this.eve = eve;
            }

            public float getMorn() {
                return morn;
            }

            public void setMorn(float morn) {
                this.morn = morn;
            }
        }

        public static class FeelsLike {
            private float day;
            private float night;
            private float eve;
            private float morn;

            // Getter and Setter
            public float getDay() {
                return day;
            }

            public void setDay(float day) {
                this.day = day;
            }

            public float getNight() {
                return night;
            }

            public void setNight(float night) {
                this.night = night;
            }

            public float getEve() {
                return eve;
            }

            public void setEve(float eve) {
                this.eve = eve;
            }

            public float getMorn() {
                return morn;
            }

            public void setMorn(float morn) {
                this.morn = morn;
            }
        }

        public static class Weather {
            private int id;
            private String main;
            private String description;
            private String icon;

            // Getter and Setter
            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMain() {
                return main;
            }

            public void setMain(String main) {
                this.main = main;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }
        }
    }
}