package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.AwayTeam
import com.ahmed.english_pl.data.models.dto.HomeTeam
import com.ahmed.english_pl.data.models.dto.Odds
import com.google.gson.Gson
import javax.inject.Inject

class OddsConverter{
    @TypeConverter
    fun fromOdds(odds: Odds?): String? {
        return Gson().toJson(odds)
    }

    @TypeConverter
    fun toOdds(json: String?): Odds? {
        return Gson().fromJson(json, Odds::class.java)
    }
}