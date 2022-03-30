package com.example.myweather.networking

import com.example.myweather.models.weatherdata.WeatherResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/data/2.5/onecall")
    suspend fun getDataByLatLong(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units:String = "metric",
        @Query("exclude") exclude: String = "minutely,alerts",
        @Query("appid") appid: String = "d331e8dc8ab75bceffe617c4fc6c9624"

    ): Response<WeatherResponse>
}