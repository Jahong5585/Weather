package com.example.myweather.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.myweather.R
import com.example.myweather.adapters.ItemDailyAdapter
import com.example.myweather.adapters.ItemHourlyAdapter
import com.example.myweather.database.AppDatabase
import com.example.myweather.databinding.CustomDialogBinding
import com.example.myweather.databinding.FragmentWholeWeekBinding
import com.example.myweather.models.weatherdata.Daily
import com.example.myweather.models.weatherdata.Hourly
import com.example.myweather.models.weatherdata.WeatherResponse
import com.example.myweather.utils.AppHelper
import com.example.myweather.utils.WeatherResource
import com.example.myweather.vm.WholeWeekViewModel
import com.example.myweather.vm.WholeWeekViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

class WholeWeekFragment : Fragment(R.layout.fragment_whole_week) {


    private val bindingF by viewBinding(FragmentWholeWeekBinding::bind)

    private lateinit var wholeWeekViewModel: WholeWeekViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var weatherResponse: WeatherResponse
    private lateinit var listHourly: ArrayList<Hourly>
    private lateinit var listDaily: ArrayList<Daily>
    private lateinit var itemHourlyAdapter: ItemHourlyAdapter
    private lateinit var itemDailyAdapter: ItemDailyAdapter
    private lateinit var binding: CustomDialogBinding
    private lateinit var appHelper: AppHelper

    var lat = 41.3040223
    var lon = 69.2917558

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())

        wholeWeekViewModel =
            ViewModelProvider(
                this,
                WholeWeekViewModelFactory(appDatabase.weatherDao())
            )[WholeWeekViewModel::class.java]

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingF.backBtnMain.setOnClickListener {
            findNavController().popBackStack()
        }



        lifecycleScope.launch {
            wholeWeekViewModel.getWeatherListLocal()
                .collect {
                    when (it) {
                        is WeatherResource.Success -> {
                            weatherResponse = WeatherResponse(
                                it.list.current,
                                it.list.daily,
                                it.list.hourly,
                                it.list.lat,
                                it.list.lon,
                                it.list.timezone,
                                it.list.timezone_offset
                            )

                            setDateWeather()
                        }
                        is WeatherResource.Error -> {

                        }
                        WeatherResource.Loading -> {

                        }
                    }
                }
        }
    }

    private fun setDateWeather() {
        appHelper = AppHelper()
        listHourly = ArrayList(weatherResponse.hourly)
        itemHourlyAdapter = ItemHourlyAdapter(listHourly)
        bindingF.rv.adapter = itemHourlyAdapter

        listDaily = ArrayList(weatherResponse.daily)
        itemDailyAdapter = ItemDailyAdapter(listDaily) {
            appHelper.showDialog(requireContext(), it, layoutInflater)
        }
        bindingF.rvDaily.adapter = itemDailyAdapter

    }

}