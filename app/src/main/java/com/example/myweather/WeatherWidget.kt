package com.example.myweather


import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import com.example.myweather.database.AppDatabase
import com.example.myweather.utils.AppHelper

class WeatherWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        val remoteViews = RemoteViews(context.packageName, R.layout.weather_widget)

        for (appWidgetId in appWidgetIds) {

            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
            val pendIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            remoteViews.setOnClickPendingIntent(R.id.widgetClick, pendIntent)

            val appDatabase = AppDatabase.getInstance(context)
            val weatherEntity = appDatabase.weatherDao().getWeather(1)
            val imageResource = AppHelper().setImageWidget(weatherEntity.current.weather[0].icon)

            remoteViews.setTextViewText(R.id.temp_widget, "${weatherEntity.current.temp.toInt()}")
            remoteViews.setTextViewText(
                R.id.date_city_name,
                AppHelper().getDate(weatherEntity.current.dt)
            )
            remoteViews.setTextViewText(
                R.id.info_weather,
                weatherEntity.current.weather[0].description
            )
            remoteViews.setImageViewResource(R.id.icon_widget, imageResource)

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val remoteViews = RemoteViews(context.packageName, R.layout.weather_widget)

    appWidgetManager.updateAppWidget(appWidgetId, remoteViews)

}