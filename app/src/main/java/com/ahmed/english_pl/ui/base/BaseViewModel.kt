package com.ahmed.english_pl.ui.base

import android.view.View
import androidx.lifecycle.*
import com.ahmed.english_pl.data.models.LoadingModel
import com.ahmed.english_pl.data.models.ProgressTypes
import com.ahmed.english_pl.data.models.Status
import com.ahmed.english_pl.utils.alternate
import com.ahmed.english_pl.utils.view_state.ViewStateHelper
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private var mSavedStateHandle: SavedStateHandle,
    private vararg var mUseCases: IBaseUseCase
) : ViewModel() {

    var loadingObservable = MutableSharedFlow<LoadingModel>()
    var errorViewObservable = MutableSharedFlow<Boolean>()
    var showToastObservable = MutableSharedFlow<String>()

    protected fun showProgress(
        shouldShow: Boolean,
        progressType: ProgressTypes = ProgressTypes.MAIN_PROGRESS
    ) {
        viewModelScope.launch {
            loadingObservable.emit(LoadingModel(shouldShow, progressType))
        }
    }

    protected fun shouldShowError(shouldShow: Boolean) {
        viewModelScope.launch {
            errorViewObservable.emit(shouldShow)
        }
    }

    protected fun showToastMessage(message: Any?, vararg args: Any?) {
        viewModelScope.launch {

            if (message is String)
                showToastObservable.emit(message)
            else
                showToastObservable.emit(message.toString().alternate())
        }
    }

    private fun <T> getStateLiveData(key: String, initialValue: T? = null): MutableLiveData<T?>? {
        if (!mSavedStateHandle.contains(key)) return null

        val liveData = mSavedStateHandle.getLiveData(key, initialValue)
        mSavedStateHandle.remove<T>(key)
        return liveData
    }

    /**
     * saves the views states using the viewModel SavedStateHandle.
     */
    fun saveStates(vararg views: View) {
        ViewStateHelper.saveViewState(mSavedStateHandle, *views)
    }


    fun <T> showErrorTitle(status: Status<T>) {
        val errorTitle = status.error
        showToastMessage(errorTitle)
    }

}
