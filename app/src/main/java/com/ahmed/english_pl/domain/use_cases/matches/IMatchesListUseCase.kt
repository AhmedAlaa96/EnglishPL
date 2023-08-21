package com.ahmed.english_pl.domain.use_cases.matches

import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IMatchesListUseCase : IBaseUseCase {
    fun getMatchesList(): Flow<Status<MatchResponse>>

    fun getFavoriteMatchesList(): ArrayList<Match>

    fun addMatchToFavorite(match: Match)

    fun removeMatchToFavorite(match: Match)
}