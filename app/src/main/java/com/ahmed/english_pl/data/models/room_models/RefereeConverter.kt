package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.*
import com.google.gson.Gson

class RefereeConverter{
    @TypeConverter
    fun fromScore(score: Referee?): String? {
        return Gson().toJson(score)
    }

    @TypeConverter
    fun toScore(json: String?): Score? {
        return Gson().fromJson(json, Score::class.java)
    }
}