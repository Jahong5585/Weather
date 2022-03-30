package com.example.myweather.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.example.myweather.R
import com.example.myweather.databinding.ItemHourlyBinding
import com.example.myweather.models.weatherdata.Hourly
import com.example.myweather.utils.AppHelper

class ItemHourlyAdapter(val list: ArrayList<Hourly>) :
    RecyclerView.Adapter<ItemHourlyAdapter.HourViewHolder>() {

    inner class HourViewHolder(var itemHourlyBinding: ItemHourlyBinding) :
        RecyclerView.ViewHolder(itemHourlyBinding.root) {
        fun onBind(hourly: Hourly, position: Int) {
            val appHelper = AppHelper()
            itemHourlyBinding.apply {
                temp.text = hourly.temp.toInt().toString() + "°"
                appHelper.setLottieAnimation(lottieAnimationView, hourly.weather[0].icon)
                hour.text = appHelper.getHour(hourly.dt)


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        return HourViewHolder(
            ItemHourlyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {

        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int {
        return list.size/2
    }
}