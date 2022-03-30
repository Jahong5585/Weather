package com.example.myweather.repository

import com.example.myweather.database.dao.WeatherDao
import com.example.myweather.database.entity.WeatherEntity
import com.example.myweather.models.weatherdata.WeatherResponse
import com.example.myweather.networking.ApiService
import com.example.myweather.utils.NetworkHelper

class WeatherRepository(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    networkHelper: NetworkHelper
) {
    //remote
    suspend fun getWeatherFromRemote(lat: Double, lon: Double, ) = apiService.getDataByLatLong(lat = lat, lon = lon)

    //local
    suspend fun addWeatherLocal(weatherEntity: WeatherEntity) = weatherDao.addWeather(weatherEntity)

    suspend fun getWeatherFromLocal() = weatherDao.getWeather(1)

    suspend fun deleteWeatherFromLocal() = weatherDao.deleteAllWeather()
}