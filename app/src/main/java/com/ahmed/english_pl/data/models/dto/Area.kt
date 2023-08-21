package com.ahmed.english_pl.data.models.dto


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class Area(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)