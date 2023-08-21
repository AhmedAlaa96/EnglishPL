package com.ahmed.english_pl.domain.use_cases.matches

import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.StatusCode
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.data.repositories.matches_list.IMatchesListRepository
import com.ahmed.english_pl.ui.base.BaseUseCase
import com.ahmed.english_pl.utils.DateTimeHelper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class MatchesListUseCase @Inject constructor(private val mMatchesListRepository: IMatchesListRepository) :
    IMatchesListUseCase, BaseUseCase(mMatchesListRepository) {
    override fun getMatchesList(): Flow<Status<MatchResponse>> {
        return mMatchesListRepository.getMatchesList()
            .mapLatest {
                return@mapLatest when (it.statusCode) {
                    StatusCode.SUCCESS -> {
                        val getFavoriteList = getFavoriteMatchesList()
                        it.data?.matches?.forEach { match: Match ->
                            if (getFavoriteList.find { favMatch ->
                                    match.id == favMatch.id
                                } != null) {
                                match.isFavorite = true
                            }
                        }
                        Status.CopyStatus(it, mapToTodayMatches(it.data?.copy()))
                    }
                    else -> {
                        it
                    }
                }
            }
    }

    override fun getFavoriteMatchesList(): ArrayList<Match> {
        return mMatchesListRepository.getFavoriteMatchesList()
    }

    override fun addMatchToFavorite(match: Match) {
        mMatchesListRepository.addMatchToFavorite(match)
    }

    override fun removeMatchToFavorite(match: Match) {
        mMatchesListRepository.removeMatchToFavorite(match)
    }

    private fun mapToTodayMatches(data: MatchResponse?): MatchResponse? {
        data?.matches?.sortBy { match -> DateTimeHelper.convertToDate(match.utcDate) }
        data?.todayMatches = data?.matches
            ?.filter { match -> DateTimeHelper.isDateTodayOrTmw(match.utcDate) }
            ?.toCollection(arrayListOf())
        data?.todayMatches?.forEach {
            it.isToday = true
        }
        data?.matches?.removeAll((data.todayMatches ?: arrayListOf()).toSet())
        return data
    }
}