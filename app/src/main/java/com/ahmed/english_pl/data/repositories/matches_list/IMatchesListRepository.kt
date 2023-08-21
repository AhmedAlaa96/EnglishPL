package com.ahmed.english_pl.data.repositories.matches_list

import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.ui.base.IBaseRepository
import kotlinx.coroutines.flow.Flow

interface IMatchesListRepository : IBaseRepository {
    fun getMatchesList(): Flow<Status<MatchResponse>>

    fun getFavoriteMatchesList(): ArrayList<Match>

    fun addMatchToFavorite(match: Match)

    fun removeMatchToFavorite(match: Match)
}