package com.example.myweather.utils

import com.example.myweather.database.entity.WeatherEntity

sealed class WeatherResource {

    object Loading : WeatherResource()
    data class Success(val list: WeatherEntity) : WeatherResource()
    data class Error(val message: String?) : WeatherResource()
}