package com.example.a10.dars.sodda.passportgeneration.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.a10.dars.sodda.passportgeneration.room.entity.Passport

@Dao
interface PassportsDao {
    @Insert
    fun insertPassport(passport: Passport)

    @Query("select * from passport")
    fun getAll(): List<Passport>
}