package com.example.myweather.vm

import androidx.lifecycle.ViewModel
import com.example.myweather.database.dao.WeatherDao
import com.example.myweather.repository.WeatherLocalRepository
import com.example.myweather.utils.WeatherResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class WholeWeekViewModel(private val weatherDao: WeatherDao) : ViewModel() {

    private val weatherLoRepository = WeatherLocalRepository(weatherDao)

    fun getWeatherListLocal(): StateFlow<WeatherResource> {

        return MutableStateFlow<WeatherResource>(WeatherResource.Success(weatherLoRepository.getWeatherFromLocal()))
    }

}