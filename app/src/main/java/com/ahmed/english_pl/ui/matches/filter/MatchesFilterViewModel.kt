package com.ahmed.english_pl.ui.matches.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.english_pl.data.models.FilterType
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.ui.base.BaseViewModel
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.DateTimeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesFilterViewModel @Inject constructor(
    handle: SavedStateHandle
) :
    BaseViewModel(handle) {

    private var matchesFilterData = MatchesFilterData()


    private val _matchesFilterAppliedMutableSharedFlow =
        MutableSharedFlow<Status<MatchesFilterData>>()
    val matchesFilterAppliedSharedFlow = _matchesFilterAppliedMutableSharedFlow.asSharedFlow()


    private val _matchFilterMutableLiveData = MutableLiveData<Status<MatchesFilterData>>()
    val matchFilterLiveData: LiveData<Status<MatchesFilterData>> = _matchFilterMutableLiveData

    fun setPreviousMatchFilter(matchesFilterData: MatchesFilterData) {
        this.matchesFilterData = matchesFilterData
        viewModelScope.launch {
            _matchFilterMutableLiveData.postValue(Status.Success(matchesFilterData))
        }
    }

    fun applyMatchFilterData() {
        viewModelScope.launch {
            if (!matchesFilterData.fromDate.isNullOrEmpty()
                && !matchesFilterData.toDate.isNullOrEmpty()
                && DateTimeHelper.isFromToDateCorrect(
                    matchesFilterData.fromDate!!,
                    matchesFilterData.toDate!!
                )
            ) {
                _matchesFilterAppliedMutableSharedFlow.emit(Status.Success(matchesFilterData))
            } else if (!matchesFilterData.fromDate.isNullOrEmpty()
                && matchesFilterData.toDate.isNullOrEmpty()
            ) {
                _matchesFilterAppliedMutableSharedFlow.emit(Status.Success(matchesFilterData))
            } else if (matchesFilterData.fromDate.isNullOrEmpty()
                && !matchesFilterData.toDate.isNullOrEmpty()
            ) {
                _matchesFilterAppliedMutableSharedFlow.emit(Status.Success(matchesFilterData))
            } else if (matchesFilterData.fromDate.isNullOrEmpty()
                && matchesFilterData.toDate.isNullOrEmpty()
            ) {
                _matchesFilterAppliedMutableSharedFlow.emit(Status.Success(matchesFilterData))
            } else {
                _matchesFilterAppliedMutableSharedFlow.emit(Status.Error(error = Constants.General.FROM_DATE_AFTER_TO_DATE))
            }


        }
    }

    fun setData(dataString: String?, type: FilterType) {
        when (type) {
            FilterType.FROM_DATE -> {
                matchesFilterData.fromDate = dataString
            }
            FilterType.TO_DATE -> {
                matchesFilterData.toDate = dataString
            }
            FilterType.STATUS -> {
                matchesFilterData.status = dataString
                matchesFilterData.setSelectedStatus()
            }
        }
    }

    fun resetMatchesFilter() {
        matchesFilterData.resetFilter()
    }

    fun getMatchesFilter() = matchesFilterData

}