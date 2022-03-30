package com.example.myweather.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myweather.database.entity.WeatherEntity

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addWeather(weatherEntityList: WeatherEntity)

    @Query("select * from WeatherEntity")
    suspend fun getWeathers(): List<WeatherEntity>

    @Query("select * from WeatherEntity where id = :id")
    fun getWeather(id: Int): WeatherEntity

    @Query("DELETE FROM WeatherEntity")
    fun deleteAllWeather()
}