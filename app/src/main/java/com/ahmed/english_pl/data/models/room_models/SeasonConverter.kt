package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.*
import com.google.gson.Gson
import javax.inject.Inject

class SeasonConverter{
    @TypeConverter
    fun fromSeason(season: Season?): String? {
        return Gson().toJson(season)
    }

    @TypeConverter
    fun toSeason(json: String?): Season? {
        return Gson().fromJson(json, Season::class.java)
    }
}