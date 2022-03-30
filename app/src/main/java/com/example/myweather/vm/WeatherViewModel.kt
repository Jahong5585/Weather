package com.example.myweather.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myweather.database.dao.WeatherDao
import com.example.myweather.database.entity.WeatherEntity
import com.example.myweather.networking.ApiService
import com.example.myweather.repository.WeatherRepository
import com.example.myweather.utils.NetworkHelper
import com.example.myweather.utils.WeatherResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherViewModel(
    private val apiService: ApiService,
    private val weatherDao: WeatherDao,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val weatherRepository =
        WeatherRepository(apiService, weatherDao, networkHelper)

    fun getWeatherList(lat: Double, lon: Double): StateFlow<WeatherResource> {
        val flow = MutableStateFlow<WeatherResource>(WeatherResource.Loading)
        viewModelScope.launch {
            try {
                if (networkHelper.isNetworkConnected()) {
                    val response = weatherRepository.getWeatherFromRemote(lat, lon)
                    if (response.isSuccessful) {
                        val list = ArrayList<WeatherEntity>()
                        val it = response.body()

                        val weatherEntity =
                            it?.let { it1 ->
                                WeatherEntity(
                                    current = it?.current,
                                    daily = it.daily,
                                    hourly = it.hourly,
                                    lat = it.lat,
                                    lon = it.lon,
                                    timezone = it.timezone,
                                    timezone_offset = it.timezone_offset
                                )
                            }
                        weatherRepository.deleteWeatherFromLocal()
                        weatherEntity?.let { it1 -> weatherRepository.addWeatherLocal(it1) }
                        flow.emit(WeatherResource.Success(weatherEntity!!))
                    } else if (response.code() in 400..499) {

                        flow.emit(WeatherResource.Error(response.errorBody()?.string()))
                    } else {
                        flow.emit(WeatherResource.Error(response.errorBody()?.string()))
                    }
                } else {
                    flow.emit(WeatherResource.Success(weatherRepository.getWeatherFromLocal()))
                }

            } catch (e: Exception) {
                Log.d("TAG", "getWeatherList: ${e.message}")
                flow.emit(WeatherResource.Error("${e.message}"))
            }
        }
        return flow
    }
}