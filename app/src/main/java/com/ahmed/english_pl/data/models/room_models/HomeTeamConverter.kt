package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.AwayTeam
import com.ahmed.english_pl.data.models.dto.HomeTeam
import com.google.gson.Gson
import javax.inject.Inject

class HomeTeamConverter{
    @TypeConverter
    fun fromHomeTeam(homeTeam: HomeTeam?): String? {
        return Gson().toJson(homeTeam)
    }

    @TypeConverter
    fun toHomeTeam(json: String?): HomeTeam? {
        return Gson().fromJson(json, HomeTeam::class.java)
    }
}