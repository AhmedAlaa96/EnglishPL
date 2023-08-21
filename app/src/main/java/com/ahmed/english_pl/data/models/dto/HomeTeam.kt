package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class HomeTeam(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
): Parcelable