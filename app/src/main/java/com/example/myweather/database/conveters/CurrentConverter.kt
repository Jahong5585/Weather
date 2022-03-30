package com.example.myweather.database.conveters

import androidx.room.TypeConverter
import com.example.myweather.models.weatherdata.Current
import com.google.gson.Gson

class CurrentConverter {

    @TypeConverter
    fun fromCurrent(current: Current): String =
        Gson().toJson(current)


    @TypeConverter
    fun toCurrent(string: String): Current =
        Gson().fromJson(string, Current::class.java)

}