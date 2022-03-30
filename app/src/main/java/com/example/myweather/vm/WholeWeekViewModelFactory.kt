package com.example.myweather.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myweather.database.dao.WeatherDao
import java.lang.RuntimeException

class WholeWeekViewModelFactory(val weatherDao: WeatherDao): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WholeWeekViewModel::class.java)) {
            return WholeWeekViewModel(weatherDao) as T
        }

        throw RuntimeException("Error")
    }


}