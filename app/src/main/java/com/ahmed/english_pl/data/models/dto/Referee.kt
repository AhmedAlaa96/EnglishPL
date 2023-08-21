package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Referee(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("nationality")
    val nationality: String?,
    @SerializedName("role")
    val role: String?
): Parcelable