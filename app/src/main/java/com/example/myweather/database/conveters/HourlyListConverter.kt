package com.example.myweather.database.conveters

import androidx.room.TypeConverter
import com.example.myweather.models.weatherdata.Daily
import com.example.myweather.models.weatherdata.Hourly
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HourlyListConverter {

    @TypeConverter
    fun fromHourlyList(list: List<Hourly>): String = Gson().toJson(list)

    @TypeConverter
    fun toHourlyList(string: String): List<Hourly> {
        val gson = Gson()
        val type = object : TypeToken<List<Hourly>>() {}.type
        return gson.fromJson(string, type)
    }
}