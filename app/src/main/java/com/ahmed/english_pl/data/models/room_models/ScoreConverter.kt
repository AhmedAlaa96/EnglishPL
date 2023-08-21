package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.AwayTeam
import com.ahmed.english_pl.data.models.dto.HomeTeam
import com.ahmed.english_pl.data.models.dto.Odds
import com.ahmed.english_pl.data.models.dto.Score
import com.google.gson.Gson
import javax.inject.Inject

class ScoreConverter{
    @TypeConverter
    fun fromScore(score: Score?): String? {
        return Gson().toJson(score)
    }

    @TypeConverter
    fun toScore(json: String?): Score? {
        return Gson().fromJson(json, Score::class.java) ?: Score()
    }
}