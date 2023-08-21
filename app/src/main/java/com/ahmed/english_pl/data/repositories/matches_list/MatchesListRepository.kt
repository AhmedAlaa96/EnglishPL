package com.ahmed.english_pl.data.repositories.matches_list

import com.ahmed.english_pl.data.local.ILocalDataSource
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.data.remote.IRemoteDataSource
import com.ahmed.english_pl.data.shared_prefrences.IPreferencesDataSource
import com.ahmed.english_pl.ui.base.BaseRepository
import com.ahmed.english_pl.utils.connection_utils.IConnectionUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MatchesListRepository @Inject constructor(
    private val connectionUtils: IConnectionUtils,
    private val mIRemoteDataSource: IRemoteDataSource,
    mILocalDataSource: ILocalDataSource,
    private val mIPreferencesDataSource: IPreferencesDataSource
) : BaseRepository(connectionUtils, mIRemoteDataSource, mIPreferencesDataSource),
    IMatchesListRepository {
    override fun getMatchesList(): Flow<Status<MatchResponse>> {
        return safeApiCalls {
            mIRemoteDataSource.getMatchData()
        }
    }

    override fun getFavoriteMatchesList(): ArrayList<Match> {
        return   mIPreferencesDataSource.loadFavoriteMatches()
    }

    override fun addMatchToFavorite(match: Match){
        mIPreferencesDataSource.insertMatch(match)
    }

    override fun removeMatchToFavorite(match: Match) {
        mIPreferencesDataSource.deleteMatch(match)
    }


}