package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class HalfTime(
    @SerializedName("awayTeam")
    val awayTeam: Int?,
    @SerializedName("homeTeam")
    val homeTeam: Int?
): Parcelable