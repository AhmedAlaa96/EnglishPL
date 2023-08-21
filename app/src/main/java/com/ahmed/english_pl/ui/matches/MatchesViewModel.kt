package com.ahmed.english_pl.ui.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.MatchesStatusFilter
import com.ahmed.english_pl.data.models.ProgressTypes
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchResponse
import com.ahmed.english_pl.domain.use_cases.match_filter.IMatchesFilterUseCase
import com.ahmed.english_pl.domain.use_cases.matches.IMatchesListUseCase
import com.ahmed.english_pl.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val mIMatchesUseCase: IMatchesListUseCase,
    private val mIMatchesFilterUseCase: IMatchesFilterUseCase,
    handle: SavedStateHandle
) :
    BaseViewModel(handle, mIMatchesUseCase) {


    var mMatchesFilterData = MatchesFilterData()

    var isFavoriteList = false


    private var matchesResponseStatus: Status<MatchResponse>? = null
    private var favoriteMatchesResponseStatus: Status<MatchResponse>? = null
    private var filteredMatchesResponseStatus: Status<MatchResponse>? = null

    private val _matchesResponseMutableSharedFlow = MutableSharedFlow<Status<MatchResponse>>()
    val matchesResponseSharedFlow = _matchesResponseMutableSharedFlow.asSharedFlow()

    private val _matchesFilterMutableSharedFlow = MutableSharedFlow<Status<MatchesFilterData>>()
    val matchesFilterSharedFlow = _matchesFilterMutableSharedFlow.asSharedFlow()

    internal fun getMatchesResponse() {
        val handler = CoroutineExceptionHandler { _, exception ->
            viewModelScope.launch {
                setMatchResponseStatus(Status.Error(error = exception.message))
            }
        }

        viewModelScope.launch(Dispatchers.Main + handler) {
            if (matchesResponseStatus != null && matchesResponseStatus?.isIdle() != true) {
                setMatchResponseStatus(matchesResponseStatus!!)
            } else {
                callGetMatches(ProgressTypes.MAIN_PROGRESS)
            }
        }
    }

    fun onItemMatchFavoriteClicked(match: Match) {
        if (match.isFavorite) {
            mIMatchesUseCase.addMatchToFavorite(match)
        } else {
            mIMatchesUseCase.removeMatchToFavorite(match)
        }
        getFavoriteClicked(isFavoriteList)
    }

    private suspend fun callGetMatches(progressType: ProgressTypes) {
        onGetMatchesSubscribe(progressType)
        mIMatchesUseCase.getMatchesList()
            .onStart {
                showProgress(true, progressType)
            }.onCompletion {
                showProgress(false)
            }.collect {
                setMatchResponseStatus(it)
            }
    }

    private fun onGetMatchesSubscribe(progressType: ProgressTypes) {
        showProgress(true, progressType)
        shouldShowError(false)
    }


    private suspend fun setMatchResponseStatus(matchResponseStatus: Status<MatchResponse>) {
        val filterStatus = (matchResponseStatus.data?.matches?.groupBy {
            it.status
        })?.keys?.toList()?.toCollection(arrayListOf())
        filterStatus?.add(0, "All")

        mMatchesFilterData = MatchesFilterData(
            status = "All",
            statusesList = filterStatus?.map { MatchesStatusFilter(it, it == "All") }?.toCollection(
                arrayListOf()
            )
        )
        matchesResponseStatus = matchResponseStatus
        _matchesResponseMutableSharedFlow.emit(matchResponseStatus)

    }

    private suspend fun setFavoriteMatchResponseStatus(matchesStatus: Status<MatchResponse>) {
        favoriteMatchesResponseStatus = matchesStatus
        _matchesResponseMutableSharedFlow.emit(matchesStatus)
    }

    private suspend fun setFilteredMatchesResponseStatus(matchesStatus: Status<MatchResponse>) {
        filteredMatchesResponseStatus = matchesStatus
        _matchesResponseMutableSharedFlow.emit(matchesStatus)
    }

    fun getFavoriteClicked(isFavoriteList: Boolean) {
        setIsFavoriteList(isFavoriteList)
        if (isFavoriteList)
            getFavoriteMatches()
        else {
            resetFavoriteFilter()
        }
    }

    private fun getFavoriteMatches() {
        viewModelScope.launch {
            onGetMatchesSubscribe(ProgressTypes.FULL_PROGRESS)
            mIMatchesFilterUseCase.getFavoriteMatchesList(mMatchesFilterData, isFavoriteList)
                .onStart {
                    showProgress(true, ProgressTypes.FULL_PROGRESS)
                }.onCompletion {
                    showProgress(false)
                }.collect {
                    setFavoriteMatchResponseStatus(it)
                }
        }
    }

    private fun setIsFavoriteList(isFavoriteList: Boolean) {
        this.isFavoriteList = isFavoriteList
    }

    private fun resetFavoriteFilter() {
        viewModelScope.launch {
            onGetMatchesSubscribe(ProgressTypes.FULL_PROGRESS)
            mIMatchesFilterUseCase.getFavoriteMatchesList(mMatchesFilterData, isFavoriteList)
                .onStart {
                    showProgress(true, ProgressTypes.FULL_PROGRESS)
                }.onCompletion {
                    showProgress(false)
                }.collect {
                    if (isFavoriteList) {
                        setFilteredMatchesResponseStatus(it)
                    } else {
                        setFavoriteMatchResponseStatus(it)
                    }
                }
        }
    }

    fun setMatchesFilterAndShowData(matchesFilterData: MatchesFilterData) {
        this.mMatchesFilterData = matchesFilterData
        viewModelScope.launch {
            _matchesFilterMutableSharedFlow.emit(Status.Success(this@MatchesViewModel.mMatchesFilterData))
            onGetMatchesSubscribe(ProgressTypes.FULL_PROGRESS)
            mIMatchesFilterUseCase.getMatchesList(matchesFilterData)
                .onStart {
                    showProgress(true, ProgressTypes.FULL_PROGRESS)
                }.onCompletion {
                    showProgress(false)
                }.collect {
                    setFilteredMatchesResponseStatus(it)
                }
        }
    }


    fun retryGetMatches() {
        viewModelScope.launch {
            callGetMatches(ProgressTypes.MAIN_PROGRESS)
        }
    }

    fun refreshGetMatches() {
        viewModelScope.launch {
            callGetMatches(ProgressTypes.PULL_TO_REFRESH_PROGRESS)
        }
    }
}