package com.example.paindiaryapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.paindiaryapp.databinding.FragmentHomePageBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageFragment extends Fragment {
    private FragmentHomePageBinding binding;
    String baseURL = "https://api.openweathermap.org/data/2.5/";
    String apiKey = "USE YOUR OWN KEY";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomePageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();




// --------------------FOR RETROFIT API, POPULATE IT IN THE HOME FRAGMENT AS WELL (there will be a better design by using parameters--------------------
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<DataWeather> call = apiService.getDataWeather("Melbourne,AU", "metric", apiKey);

        call.enqueue(new Callback<DataWeather>() {
            @Override
            public void onResponse(Call<DataWeather> call, Response<DataWeather> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Error code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }

                String temp = response.body().getMain().getTemp();
                String humidity = response.body().getMain().getHumidity();
                String pressure = response.body().getMain().getPressure();
                String weatherStatus = response.body().getWeathers().get(0).getMain();

                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Weather", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();
                spEditor.putString("Temperature", temp);
                spEditor.putString("Humidity", humidity);
                spEditor.putString("Pressure", pressure);
                spEditor.apply();

//                TextView tempText = findViewById(R.id.temperature);
//                TextView humidityText = findViewById(R.id.humidity);
//                TextView pressureText = findViewById(R.id.pressure);
//                TextView weatherStatusText = findViewById(R.id.weatherStatus);
//                ImageView weatherIcon = findViewById(R.id.weatherIcon);

                binding.temperature.setText(temp + "°C");
                binding.humidity.setText("Humidity: " + humidity + "%");
                binding.pressure.setText("Pressure: " + pressure + "mb");
                binding.weatherStatus.setText(weatherStatus);

                switch (weatherStatus) {
                    case "Thunderstorm":
                        binding.weatherIcon.setImageResource(R.drawable.thunderstorm);
                        break;
                    case "Drizzle":
                        binding.weatherIcon.setImageResource(R.drawable.drizzle);
                        break;
                    case "Rain":
                        binding.weatherIcon.setImageResource(R.drawable.rain);
                        break;
                    case "Snow":
                        binding.weatherIcon.setImageResource(R.drawable.snow);
                        break;
                    case "Atmosphere":
                        binding.weatherIcon.setImageResource(R.drawable.atmosphere);
                        break;
                    case "Clear":
                        binding.weatherIcon.setImageResource(R.drawable.clear);
                        break;
                    case "Clouds":
                        binding.weatherIcon.setImageResource(R.drawable.clouds);
                        break;
                }

            }

            @Override
            public void onFailure(Call<DataWeather> call, Throwable t) {
                Toast.makeText(getActivity(), "Fail to retrieve weather data", Toast.LENGTH_LONG).show();
            }
        });

// --------------------FOR RETROFIT API, POPULATE IT IN THE HOME FRAGMENT AS WELL (there will be a better design by using parameters--------------------

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
