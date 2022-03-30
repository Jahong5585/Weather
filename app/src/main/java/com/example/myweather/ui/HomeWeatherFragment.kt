package com.example.myweather.ui

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.airbnb.lottie.LottieAnimationView
import com.example.myweather.R
import com.example.myweather.database.AppDatabase
import com.example.myweather.databinding.FragmentHomeWeatherBinding
import com.example.myweather.models.weatherdata.Daily
import com.example.myweather.models.weatherdata.WeatherResponse
import com.example.myweather.networking.ApiClient
import com.example.myweather.utils.AppHelper
import com.example.myweather.utils.NetworkHelper
import com.example.myweather.utils.WeatherResource
import com.example.myweather.vm.WeatherViewModel
import com.example.myweather.vm.WeatherViewModelFactory
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeWeatherFragment : Fragment(R.layout.fragment_home_weather) {

    private val binding by viewBinding(FragmentHomeWeatherBinding::bind)

    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var appDatabase: AppDatabase
    private lateinit var networkHelper: NetworkHelper
    private lateinit var weatherResponse: WeatherResponse
    private lateinit var appHelper: AppHelper

    var lat = 41.3040223
    var lon = 69.2917558

    private val TAG = "TAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        networkHelper = NetworkHelper(requireContext())
        appHelper = AppHelper()
        weatherViewModel = ViewModelProvider(
            this,
            WeatherViewModelFactory(
                ApiClient.apiService,
                appDatabase.weatherDao(),
                networkHelper
            )
        )[WeatherViewModel::class.java]
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bundle>("key")
            ?.observe(viewLifecycleOwner) { result ->
                lat = result.getDouble("lat")
                lon = result.getDouble("lon")
            }

        binding.allWeekly.setOnClickListener {
            findNavController().navigate(R.id.wholeWeekFragment)
        }

        binding.mapImage.setOnClickListener {
            findNavController().navigate(R.id.mapsFragment)
        }
    }

    private fun setDataWeather() {
        binding.nameCity.text = weatherResponse.timezone
        setMainDataWeather()
        setAirQuality()
        setWeekly()
    }

    private fun setWeekly() {
        binding.apply {
            setFirsItemWeekly(weatherResponse.daily[0], weekFirst, dateFirst, iconFirst, tempFirst)
            setFirsItemWeekly(
                weatherResponse.daily[1],
                weekSecond,
                dateSecond,
                iconSecond,
                tempSecond
            )
            setFirsItemWeekly(weatherResponse.daily[2], weekThird, dateThird, iconThird, tempThird)
            setFirsItemWeekly(
                weatherResponse.daily[3],
                weekFourth,
                dateFourth,
                iconFourth,
                tempFourth
            )

            firstItemView.setOnClickListener {
                appHelper.showDialog(requireContext(), weatherResponse.daily[0], layoutInflater)
            }
            secondItemView.setOnClickListener {
                appHelper.showDialog(requireContext(), weatherResponse.daily[1], layoutInflater)
            }
            thirdItemView.setOnClickListener {
                appHelper.showDialog(requireContext(), weatherResponse.daily[2], layoutInflater)
            }
            fourthItemView.setOnClickListener {
                appHelper.showDialog(requireContext(), weatherResponse.daily[3], layoutInflater)
            }

        }
    }

    private fun setFirsItemWeekly(
        daily: Daily,
        week: TextView,
        date: TextView,
        icon: LottieAnimationView,
        temp: TextView
    ) {
        binding.apply {
            week.text = appHelper.getWeekName(daily.dt)
            date.text = getDateMonth(daily.dt)
            appHelper.setLottieAnimation(icon, daily.weather[0].icon)
            temp.text = daily.temp.day.toInt().toString() + "°"

        }
    }


    private fun getDateMonth(dt: Int): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM", Locale.getDefault())
        return format.format(date)
    }


    private fun setAirQuality() {
        binding.apply {
            val info = weatherResponse.current
            realFeel.text = info.feels_like.toInt().toString()
            windSpeed.text = "${info.wind_speed}m/s"
            humidity.text = "${info.humidity}%"
            when {
                info.uvi.toInt() in 0..3 -> {
                    UVIndex.setTextColor(Color.GREEN)
                }
                info.uvi.toInt() in 4..6 -> {
                    UVIndex.setTextColor(Color.YELLOW)
                }
                info.uvi.toInt() in 7..9 -> {
                    UVIndex.setTextColor(Color.parseColor("#ff752b"))
                }
                info.uvi.toInt() in 10..15 -> {
                    UVIndex.setTextColor(Color.RED)
                }
            }
            UVIndex.text = info.uvi.toInt().toString()
            visibility.text = "${info.visibility}m"


        }
    }

    private fun setMainDataWeather() {
        binding.apply {

            val info = weatherResponse.current
            appHelper.setLottieAnimation(
                binding.animationWeather,
                weatherResponse.current.weather[0].icon
            )
            tempText.text = info.temp.toInt().toString() + "°"
            weatherDescription.text = info.weather[0].description
            dateWeek.text = appHelper.getDate(info.dt)

        }
    }

    private fun setTextGradient() {
        val textView = binding.tempText

        val paint: TextPaint = textView.paint
        val width = paint.measureText("21")

        val textShader: Shader = LinearGradient(
            0.0f, (textView.height / 2).toFloat(), 0f, textView.textSize, intArrayOf(
                resources.getColor(R.color.white),
                resources.getColor(R.color.white),
                resources.getColor(R.color.end_temp_text)
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            weatherViewModel.getWeatherList(lat, lon)
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
                            binding.loadingAnimation.visibility = View.GONE
                            binding.allWeekly.visibility = View.VISIBLE
                            setTextGradient()
                            setDataWeather()
                        }

                        is WeatherResource.Loading -> {
                            setTextGradient()

                        }

                        is WeatherResource.Error -> {
                            setTextGradient()
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

    }

}