package com.example.myweather.adapters

import android.content.res.Resources
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.ItemDailyBinding
import com.example.myweather.databinding.ItemHourlyBinding
import com.example.myweather.models.weatherdata.Daily
import com.example.myweather.models.weatherdata.Hourly
import com.example.myweather.utils.AppHelper

class ItemDailyAdapter(val list: ArrayList<Daily>, val click:(Daily) -> Unit) :
    RecyclerView.Adapter<ItemDailyAdapter.DayViewHolder>() {



    inner class DayViewHolder(private val itemDailyBinding: ItemDailyBinding) :
        RecyclerView.ViewHolder(itemDailyBinding.root) {
        fun onBind(daily: Daily, position: Int) {
            val appHelper = AppHelper()
            if (position%2 != 0) {
                itemDailyBinding.apply {
                    mainLay.setBackgroundColor(Color.WHITE)
                    weekName.setTextColor(Color.BLACK)
                    date.setTextColor(Color.BLACK)
                    temp.setTextColor(Color.BLACK)
                    backImage.visibility = View.GONE
                }
            }

            itemDailyBinding.apply {
                weekName.text = appHelper.getWeekRas(daily.dt)
                date.text = appHelper.getDateDes(daily.dt.toLong())
                temp.text = daily.temp.day.toInt().toString() + "°"
                appHelper.setLottieAnimation(lottieAnimationView, daily.weather[0].icon)

            }

            itemView.setOnClickListener {
                click.invoke(daily)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            ItemDailyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {

        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}