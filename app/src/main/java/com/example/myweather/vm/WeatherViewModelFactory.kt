package com.example.myweather.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.database.dao.WeatherDao
import com.example.myweather.networking.ApiService
import com.example.myweather.utils.NetworkHelper
import java.lang.RuntimeException

class WeatherViewModelFactory(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    private val networkHelper: NetworkHelper
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            return WeatherViewModel(apiService, weatherDao, networkHelper) as T
        }

        throw RuntimeException("Error")
    }
}