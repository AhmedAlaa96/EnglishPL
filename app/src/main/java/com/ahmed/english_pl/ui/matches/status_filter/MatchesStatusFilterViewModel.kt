package com.ahmed.english_pl.ui.matches.status_filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.english_pl.data.models.FilterType
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.ui.base.BaseViewModel
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.DateTimeHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesStatusFilterViewModel @Inject constructor(
    handle: SavedStateHandle
) :
    BaseViewModel(handle) {

    private var matchesFilterData = MatchesFilterData()


    private val _matchesFilterMutableSharedFlow = MutableLiveData<Status<MatchesFilterData>>()
    val matchesFilterSharedFlow: LiveData<Status<MatchesFilterData>> =
        _matchesFilterMutableSharedFlow

    private val _matchesFilterAppliedMutableSharedFlow =
        MutableSharedFlow<Status<MatchesFilterData>>()
    val matchesFilterAppliedSharedFlow = _matchesFilterAppliedMutableSharedFlow.asSharedFlow()

    fun setPreviousMatchFilter(matchesFilterData: MatchesFilterData) {
        viewModelScope.launch {
            this@MatchesStatusFilterViewModel.matchesFilterData = matchesFilterData
            _matchesFilterMutableSharedFlow.postValue(Status.Success(this@MatchesStatusFilterViewModel.matchesFilterData))
        }
    }

    fun setData(dataString: String?) {
        viewModelScope.launch {
            matchesFilterData.status = dataString
            matchesFilterData.setSelectedStatus()
            _matchesFilterAppliedMutableSharedFlow.emit(Status.Success(matchesFilterData))
        }
    }

}