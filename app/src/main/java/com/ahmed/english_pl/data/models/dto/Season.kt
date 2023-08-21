package com.ahmed.english_pl.data.models.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity
data class Season(
    @SerializedName("currentMatchday")
    val currentMatchDay: Int?,
    @SerializedName("endDate")
    val endDate: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("startDate")
    val startDate: String?
): Parcelable