package com.ahmed.english_pl.data.models.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import com.ahmed.english_pl.data.models.MatchesFilterData

@Keep
data class MatchResponse(
    @SerializedName("competition")
    val competition: Competition? = null,
    @SerializedName("count")
    val count: Int? = null,
    @SerializedName("filters")
    val filters: Filters? = null,
    @SerializedName("matches")
    var matches: ArrayList<Match>? = null,
    var todayMatches: ArrayList<Match>? = null,
)