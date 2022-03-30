package com.example.myweather.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myweather.models.weatherdata.Current
import com.example.myweather.models.weatherdata.Daily
import com.example.myweather.models.weatherdata.Hourly

@Entity
data class WeatherEntity(
    @PrimaryKey()
    val id: Int = 1,

    val current: Current,
    val daily: List<Daily>,
    val hourly: List<Hourly>,
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int
)