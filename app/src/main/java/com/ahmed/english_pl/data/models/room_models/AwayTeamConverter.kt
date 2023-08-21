package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.AwayTeam
import com.google.gson.Gson

class AwayTeamConverter {
    @TypeConverter
    fun fromAwayTeam(awayTeam: AwayTeam?): String? {
        return Gson().toJson(awayTeam)
    }

    // Convert a String to an AwayTeam object
    @TypeConverter
    fun toAwayTeam(json: String?): AwayTeam? {
        return Gson().fromJson(json, AwayTeam::class.java)
    }
}