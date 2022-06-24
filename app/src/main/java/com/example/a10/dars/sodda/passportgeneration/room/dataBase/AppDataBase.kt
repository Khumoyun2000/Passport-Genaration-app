package com.example.a10.dars.sodda.passportgeneration.room.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.a10.dars.sodda.passportgeneration.room.dao.PassportsDao
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport

@Database(entities = [Passport::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun passportsDao(): PassportsDao

    companion object {
        private var appDataBase: AppDataBase? = null

        @Synchronized
        fun getInstance(context: Context): AppDataBase {
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(context, AppDataBase::class.java, "Passport.db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return appDataBase!!
        }

    }
}