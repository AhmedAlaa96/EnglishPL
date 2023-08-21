package com.ahmed.english_pl.data.remote

import com.ahmed.english_pl.data.models.dto.MatchResponse

interface IRemoteDataSource {
    suspend fun getMatchData(): MatchResponse
}