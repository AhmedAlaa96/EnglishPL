package com.ahmed.english_pl.domain.use_cases.match_filter

import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.StatusCode
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.data.repositories.matches_list.IMatchesListRepository
import com.ahmed.english_pl.domain.use_cases.matches.IMatchesListUseCase
import com.ahmed.english_pl.ui.base.BaseUseCase
import com.ahmed.english_pl.utils.DateTimeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import java.util.ArrayList
import javax.inject.Inject

class MatchesFilterUseCase @Inject constructor(private val mMatchesListUseCase: IMatchesListUseCase) :
    IMatchesFilterUseCase {


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

    override fun getMatchesList(matchesFilterData: MatchesFilterData): Flow<Status<MatchResponse>> {
        return mMatchesListUseCase.getMatchesList()
            .mapLatest { matchesResponseStatus ->
                if (matchesFilterData.needToFilter()) {
                    val matchResponse = matchesResponseStatus.data?.copy()
                    matchResponse?.matches = matchResponse?.matches?.filter { match ->
                        DateTimeHelper.inFilterData(match, matchesFilterData)
                    }?.toCollection(arrayListOf())
                    matchResponse?.todayMatches = matchResponse?.todayMatches?.filter { match ->
                        DateTimeHelper.inFilterData(
                            match,
                            matchesFilterData
                        )
                    }?.toCollection(arrayListOf())
                    Status.CopyStatus(matchesResponseStatus, matchResponse)
                } else {
                    matchesResponseStatus
                }
            }
    }

    override fun getFavoriteMatchesList(matchesFilterData: MatchesFilterData, isFavoriteList: Boolean): Flow<Status<MatchResponse>> {
        return getMatchesList(matchesFilterData)
            .mapLatest { matchesResponseStatus ->
                val matchResponse = matchesResponseStatus.data
                if (isFavoriteList) {
                    val favoriteMatches = mMatchesListUseCase.getFavoriteMatchesList()
                    val favMatchResponse = matchResponse?.copy(
                        matches = filterMatchesWithFavorite(
                            matchResponse.matches,
                            favoriteMatches
                        ),
                        todayMatches = filterMatchesWithFavorite(
                            matchResponse.todayMatches,
                            favoriteMatches
                        )
                    )
                    Status.Success(favMatchResponse)
                } else {
                    Status.Success(matchResponse)
                }
            }
    }

    private fun filterMatchesWithFavorite(
        matches: ArrayList<Match>?,
        favoriteMatches: ArrayList<Match>
    ): ArrayList<Match>? {
        return matches?.filter { match ->
            favoriteMatches.find { favMatch ->
                match.id == favMatch.id
            } != null
        }?.toCollection(arrayListOf())
    }
}