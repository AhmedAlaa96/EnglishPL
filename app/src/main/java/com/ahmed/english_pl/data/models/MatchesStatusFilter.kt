package com.ahmed.english_pl.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MatchesStatusFilter(
    val status: String?,
    var isSelected:Boolean = false
) : Parcelable
