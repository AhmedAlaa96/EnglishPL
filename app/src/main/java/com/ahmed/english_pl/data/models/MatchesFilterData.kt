package com.ahmed.english_pl.data.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class MatchesFilterData(
    var fromDate: String? = null,
    var toDate: String? = null,
    var status: String? = null,
    val statusesList: ArrayList<MatchesStatusFilter>? = null
) : Parcelable {

    fun setSelectedStatus() {
        statusesList?.forEach {
            it.isSelected = it.status == status
        }
    }

    fun needToFilter(): Boolean = !fromDate.isNullOrEmpty() || !toDate.isNullOrEmpty() || (!status.isNullOrEmpty() &&status != "All" )

    fun resetFilter() {
        fromDate = null
        toDate = null
        status = null
    }
}
