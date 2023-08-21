package com.ahmed.english_pl.data.models.room_models

import androidx.room.TypeConverter
import com.ahmed.english_pl.data.models.dto.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RefereesConverter{
    @TypeConverter
    fun fromArrayList(list: ArrayList<Referee>?): String? {
        return Gson().toJson(list)
    }

    // Convert a String to an ArrayList object
    @TypeConverter
    fun toArrayList(json: String?): ArrayList<String>? {
        return Gson().fromJson(json, object : TypeToken<ArrayList<Referee>?>() {}.type)
    }
}