package com.example.myweather.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.myweather.R
import com.example.myweather.databinding.CustomDialogBinding
import com.example.myweather.models.weatherdata.Daily
import java.text.SimpleDateFormat
import java.util.*

open class AppHelper {

    open fun getDate(dt: Int): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat(", dd.MM.yyyy", Locale.getDefault())
        val formatN = SimpleDateFormat("EEEE", Locale.getDefault())
        val date1 = format.format(date)
        return when (formatN.format(date)) {
            "понедельник" -> {
                "Monday $date1"
            }
            "вторник" -> {
                "Tuesday $date1"
            }
            "среда" -> {
                "Wednesday $date1"
            }
            "четверг" -> {
                "Thursday $date1"
            }
            "пятница" -> {
                "Friday $date1"
            }
            "суббота" -> {
                "Saturday $date1"
            }
            "воскресенье" -> {
                "Sunday $date1"
            }
            else -> "${formatN.format(date)} $date1"
        }
    }

    open fun getWeekRas(dt: Int): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val formatN = SimpleDateFormat("EEEE", Locale.getDefault())
        return when (formatN.format(date)) {
            "понедельник" -> {
                "Monday"
            }
            "вторник" -> {
                "Tuesday"
            }
            "среда" -> {
                "Wednesday"
            }
            "четверг" -> {
                "Thursday"
            }
            "пятница" -> {
                "Friday"
            }
            "суббота" -> {
                "Saturday"
            }
            "воскресенье" -> {
                "Sunday"
            }
            else -> formatN.format(date)
        }
    }

    open fun getWeekName(dt: Int): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("EEE", Locale.getDefault())
        return when (format.format(date)) {
            "пн" -> {
                "Mon"
            }
            "вт" -> {
                "Tue"
            }
            "ср" -> {
                "Wed"
            }
            "чт" -> {
                "Thu"
            }
            "пт" -> {
                "Fri"
            }
            "сб" -> {
                "Sat"
            }
            "вс" ->{
                "Sun"
            }
            else -> {
                format.format(date)
            }
        }
    }

    open fun getHour(dt: Long): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())

        return format.format(date)
    }

    open fun getDateDes(dt: Long): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM", Locale.getDefault())

        return format.format(date)
    }

    open fun setLottieAnimation(lottie: LottieAnimationView, icon: String) {

        if (icon == "01d") {
            lottie.setAnimation(R.raw.sun_icon)
        } else if (icon == "02d" || icon == "02n") {
            lottie.setAnimation(R.raw.weather)
        } else if (icon == "03d" || icon == "04d" || icon == "03n" || icon == "04n") {
            lottie.setAnimation(R.raw.cloudy)
        } else if (icon == "09d" || icon == "09n") {
            lottie.setAnimation(R.raw.rainstorm)
        } else if (icon == "10d" || icon == "10n") {
            lottie.setAnimation(R.raw.rainstorm)
        } else if (icon == "11d" || icon == "11n") {
            lottie.setAnimation(R.raw.thunderstorm)
        } else if (icon == "13d" || icon == "13n") {
            lottie.setAnimation(R.raw.weather_snow)
        } else if (icon == "50d" || icon == "50n") {
            lottie.setAnimation(R.raw.mist)
        } else if (icon == "01n") {
            lottie.setAnimation(R.raw.moon)
        }

        lottie.playAnimation()
        lottie.loop(true)

    }


    open fun setImageWidget(icon: String): Int {

        if (icon == "01d") {
            return R.drawable.clear_sky_d
        } else if (icon == "02d") {
            return R.drawable.few_clouds_d
        } else if (icon == "02n") {
            return R.drawable.few_clouds_n
        } else if (icon == "03d" || icon == "03n") {
            return R.drawable.scattered_clouds_dn
        } else if (icon == "04d" || icon == "04n") {
            return R.drawable.broken_clouds_dn
        } else if (icon == "09d" || icon == "09n") {
            return R.drawable.shower_rain_dn
        } else if (icon == "10d") {
            return R.drawable.rain_d
        } else if (icon == "10n") {
            return R.drawable.rain_n
        } else if (icon == "11d" || icon == "11n") {
            return R.drawable.thunderstorm_dn
        } else if (icon == "13d" || icon == "13n") {
            return R.drawable.snow_dn
        } else if (icon == "50d" || icon == "50n") {
            return R.drawable.mist_dn
        } else {
            return R.drawable.clear_sky_n
        }

    }


    fun showDialog(context: Context, daily: Daily, layoutInflater: LayoutInflater) {
        val builder = AlertDialog.Builder(context)
        val binding = CustomDialogBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val dialog = builder.create()
        setDateDialog(daily, binding)
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    }

    private fun setDateDialog(daily: Daily, binding: CustomDialogBinding) {
        setMainDataWeather(daily, binding)
        setAirQuality(daily, binding)

    }

    private fun getDateMonth(dt: Int): String {
        val time = dt * 1000.toLong()
        val date = Date(time)
        val format = SimpleDateFormat("dd.MM", Locale.getDefault())
        return format.format(date)
    }


    private fun setAirQuality(daily: Daily, binding: CustomDialogBinding) {
        binding.apply {
            realFeel.text = daily.feels_like.day.toInt().toString()
            windSpeed.text = "${daily.wind_speed}m/s"
            humidity.text = "${daily.humidity}%"
            when {
                daily.uvi.toInt() in 0..3 -> {
                    UVIndex.setTextColor(Color.GREEN)
                }
                daily.uvi.toInt() in 4..6 -> {
                    UVIndex.setTextColor(Color.YELLOW)
                }
                daily.uvi.toInt() in 7..9 -> {
                    UVIndex.setTextColor(Color.parseColor("#ff752b"))
                }
                daily.uvi.toInt() in 10..15 -> {
                    UVIndex.setTextColor(Color.RED)
                }
            }
            UVIndex.text = daily.uvi.toInt().toString()

        }
    }

    private fun setMainDataWeather(daily: Daily, binding: CustomDialogBinding) {
        binding.apply {

            setLottieAnimation(
                binding.animationWeather,
                daily.weather[0].icon
            )
            tempText.text = daily.temp.day.toInt().toString() + "°"
            weatherDescription.text = daily.weather[0].description
            dateWeek.text = getDate(daily.dt)


        }
    }


}