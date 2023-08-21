package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Penalties(
    @SerializedName("awayTeam")
    val awayTeam: Int? = null,
    @SerializedName("homeTeam")
    val homeTeam: Int? = null
): Parcelable