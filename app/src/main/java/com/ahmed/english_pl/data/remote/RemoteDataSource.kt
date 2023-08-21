package com.ahmed.english_pl.data.remote

import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.retrofit.ApiInterface

class RemoteDataSource(private val mRetrofitInterface: ApiInterface) : IRemoteDataSource {
    override suspend fun getMatchData(): MatchResponse {
        return mRetrofitInterface.getMatchData()
    }

}