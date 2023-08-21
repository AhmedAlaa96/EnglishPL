package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Match(
    @SerializedName("awayTeam")
    val awayTeam: AwayTeam? = null,
    @SerializedName("group")
    val group: String? = null,
    @SerializedName("homeTeam")
    val homeTeam: HomeTeam? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("lastUpdated")
    val lastUpdated: String? = null,
    @SerializedName("matchday")
    val matchday: Int? = null,
    @SerializedName("odds")
    val odds: Odds? = null,
    @SerializedName("referees")
    val referees: ArrayList<Referee>? = null,
    @SerializedName("score")
    val score: Score? = null,
    @SerializedName("season")
    val season: Season? = null,
    @SerializedName("stage")
    val stage: String? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("utcDate")
    val utcDate: String? = null,
    @ColumnInfo(name = "is_favorite")
    var isFavorite: Boolean = false,
    @ColumnInfo(name = "is_today")
    var isToday: Boolean = false,

    ) : Parcelable