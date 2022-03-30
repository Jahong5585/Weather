package com.example.myweather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myweather.database.conveters.CurrentConverter
import com.example.myweather.database.conveters.DailyListConverter
import com.example.myweather.database.conveters.HourlyListConverter
import com.example.myweather.database.dao.WeatherDao
import com.example.myweather.database.entity.WeatherEntity

@Database(entities = [WeatherEntity::class], version = 1)
@TypeConverters(CurrentConverter::class, DailyListConverter::class, HourlyListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {
        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return appDatabase!!
        }
    }
}