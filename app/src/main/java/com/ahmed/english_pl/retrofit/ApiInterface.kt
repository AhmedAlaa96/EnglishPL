package com.ahmed.english_pl.retrofit

import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.utils.Constants.URL.GET_MATCHES
import retrofit2.http.*


interface ApiInterface {

    @GET(GET_MATCHES)
    suspend fun getMatchData(): MatchResponse
}
