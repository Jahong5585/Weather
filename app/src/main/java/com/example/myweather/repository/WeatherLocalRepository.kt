package com.example.myweather.repository

import com.example.myweather.database.dao.WeatherDao

class WeatherLocalRepository(private val weatherDao: WeatherDao) {

    fun getWeatherFromLocal() = weatherDao.getWeather(1)
}