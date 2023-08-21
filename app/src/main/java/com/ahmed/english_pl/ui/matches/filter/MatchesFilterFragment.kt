package com.ahmed.english_pl.ui.matches.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.ahmed.english_pl.R
import com.ahmed.english_pl.data.models.FilterType
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.databinding.FragmentMatchesFilterBinding
import com.ahmed.english_pl.ui.base.BaseBottomSheetFragment
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.Constants.FragmentListeners.FILTER_BUNDLE
import com.ahmed.english_pl.utils.Constants.FragmentListeners.FILTER_KEY
import com.ahmed.english_pl.utils.Constants.FragmentListeners.STATUS_BUNDLE
import com.ahmed.english_pl.utils.Constants.FragmentListeners.STATUS_KEY
import com.ahmed.english_pl.utils.DateTimeHelper
import com.ahmed.english_pl.utils.alternate
import com.ahmed.english_pl.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesFilterFragment : BaseBottomSheetFragment<FragmentMatchesFilterBinding>() {


    companion object {
        const val TAG = "MatchesFilterFragment"
        private const val MATCH_FILTER_DATA = "matchesFilterData"

        internal fun newInstance(matchesFilterData: MatchesFilterData): MatchesFilterFragment {
            return MatchesFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MATCH_FILTER_DATA, matchesFilterData)
                }
            }
        }
    }

    private val mMatchFilterViewModel: MatchesFilterViewModel by viewModels()

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesFilterBinding
        get() = FragmentMatchesFilterBinding::inflate

    override val styleResId: Int
        get() = R.style.CustomBottomSheetDialogTheme

    override val disableCollapsing: Boolean
        get() = false

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setFragmentResultListener(STATUS_KEY) { _, bundle ->
            val matchFilter = bundle.getParcelable<MatchesFilterData>(STATUS_BUNDLE)
            mMatchFilterViewModel.setData(matchFilter?.status, FilterType.STATUS)
            binding.statusText.text = matchFilter?.status.alternate()
        }
    }

    override fun initViews() {

    }

    override fun setListeners() {
        binding.viewToolbar.imgClose.setOnClickListener {
            dismiss()
        }

        binding.viewToolbar.txtReset.setOnClickListener {
            binding.fromDateText.text = getString(R.string.pick_a_date)
            binding.toDateText.text = getString(R.string.pick_a_date)
            binding.statusText.text = getString(R.string.all)
            mMatchFilterViewModel.resetMatchesFilter()
            mMatchFilterViewModel.applyMatchFilterData()
        }

        binding.dtpFromDate.setOnClickListener {
            DateTimeHelper.openDatePickerDialog(
                mContext,
                binding.fromDateText,
            ) {
                mMatchFilterViewModel.setData(it, FilterType.FROM_DATE)
            }
        }

        binding.dtpToDate.setOnClickListener {
            DateTimeHelper.openDatePickerDialog(
                mContext,
                binding.toDateText,
            ) {
                mMatchFilterViewModel.setData(it, FilterType.TO_DATE)
            }
        }

        binding.linStatus.setOnClickListener {
            navigateTo(
                MatchesFilterFragmentDirections.actionToMatchesStatusFilterFragment(
                    mMatchFilterViewModel.getMatchesFilter()
                )
            )
        }

        binding.btnFilter.setOnClickListener {
            mMatchFilterViewModel.applyMatchFilterData()
        }

    }

    override fun initViewModels(arguments: Bundle?) {
        val args: MatchesFilterFragmentArgs by navArgs()
        mMatchFilterViewModel.setPreviousMatchFilter(args.matchesFilterData)
    }

    override fun bindViewModels() {
        observe(mMatchFilterViewModel.matchesFilterAppliedSharedFlow) {
            if (it.isSuccess()) {
                setFragmentResult(FILTER_KEY, bundleOf(FILTER_BUNDLE to it.data))
                onBtnClickedRetrieved(true)
            } else {
                if (it.error == Constants.General.FROM_DATE_AFTER_TO_DATE)
                    showMessage(getString(R.string.from_after_to))
                else
                    showMessage(it.error.alternate())
            }
        }

        observe(mMatchFilterViewModel.matchFilterLiveData) {
            if (it.isSuccess()) {
                binding.fromDateText.text =
                    DateTimeHelper.convertDateStringToAnotherFormat(it.data?.fromDate)
                        ?.alternate(getString(R.string.pick_a_date))
                binding.toDateText.text =
                    DateTimeHelper.convertDateStringToAnotherFormat(it.data?.toDate)
                        ?.alternate(getString(R.string.pick_a_date))
                binding.statusText.text = it.data?.status?.alternate(getString(R.string.all))
            }
        }
    }

    override fun showError(shouldShow: Boolean) {
    }


    private fun onBtnClickedRetrieved(isSuccess: Boolean) {
        if (isSuccess)
            dismiss()
    }
}