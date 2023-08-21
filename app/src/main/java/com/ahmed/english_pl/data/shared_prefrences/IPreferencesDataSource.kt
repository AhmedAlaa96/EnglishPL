package com.ahmed.english_pl.data.shared_prefrences

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.ahmed.english_pl.data.models.dto.Match


interface IPreferencesDataSource {

    fun loadFavoriteMatches(): ArrayList<Match>

    fun insertMatch(match: Match?)

    fun deleteMatch(match: Match?)
}