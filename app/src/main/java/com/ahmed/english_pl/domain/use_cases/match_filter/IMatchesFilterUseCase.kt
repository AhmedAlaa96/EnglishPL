package com.ahmed.english_pl.domain.use_cases.match_filter

import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.ui.base.IBaseUseCase
import kotlinx.coroutines.flow.Flow

interface IMatchesFilterUseCase : IBaseUseCase {
    fun getMatchesList(matchesFilterData: MatchesFilterData): Flow<Status<MatchResponse>>

    fun getFavoriteMatchesList(matchesFilterData: MatchesFilterData, isFavoriteList: Boolean): Flow<Status<MatchResponse>>

}