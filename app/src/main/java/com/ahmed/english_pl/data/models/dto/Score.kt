package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Score(
    @SerializedName("duration")
    val duration: String? = null,
    @SerializedName("extraTime")
    val extraTime: ExtraTime? = null,
    @SerializedName("fullTime")
    val fullTime: FullTime? = null,
    @SerializedName("halfTime")
    val halfTime: HalfTime? = null,
    @SerializedName("penalties")
    val penalties: Penalties? = null,
    @SerializedName("winner")
    val winner: String? = null
): Parcelable