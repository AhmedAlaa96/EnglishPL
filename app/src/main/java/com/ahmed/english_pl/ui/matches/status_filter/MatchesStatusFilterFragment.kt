package com.ahmed.english_pl.ui.matches.status_filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.english_pl.R
import com.ahmed.english_pl.data.models.FilterType
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.MatchesStatusFilter
import com.ahmed.english_pl.databinding.FragmentStatusFilterBinding
import com.ahmed.english_pl.ui.base.BaseBottomSheetFragment
import com.ahmed.english_pl.ui.base.ListItemClickListener
import com.ahmed.english_pl.ui.matches.status_filter.adapter.StatusesAdapter
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.Constants.FragmentListeners.STATUS_BUNDLE
import com.ahmed.english_pl.utils.alternate
import com.ahmed.english_pl.utils.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MatchesStatusFilterFragment : BaseBottomSheetFragment<FragmentStatusFilterBinding>(),
    ListItemClickListener<MatchesStatusFilter> {


    companion object {
        const val TAG = "MatchesFilterFragment"
        private const val MATCH_FILTER_DATA = "matchesFilterData"

        internal fun newInstance(matchesFilterData: MatchesFilterData): MatchesStatusFilterFragment {
            return MatchesStatusFilterFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(MATCH_FILTER_DATA, matchesFilterData)
                }
            }
        }
    }

    private val mMatchFilterViewModel: MatchesStatusFilterViewModel by viewModels()
    private lateinit var statusesAdapter: StatusesAdapter

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentStatusFilterBinding
        get() = FragmentStatusFilterBinding::inflate

    override val styleResId: Int
        get() = R.style.CustomBottomSheetDialogTheme

    override val disableCollapsing: Boolean
        get() = false

    override fun onActivityReady(savedInstanceState: Bundle?) {
    }

    override fun initViews() {
        initStatuesRecyclerView()
    }

    override fun setListeners() {
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    private fun initStatuesRecyclerView() {
        statusesAdapter = StatusesAdapter(mContext, this)
        val mStatusesLayoutManager =
            LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        binding.rvStatusesFilter.layoutManager = mStatusesLayoutManager
        binding.rvStatusesFilter.adapter = statusesAdapter
    }


    override fun initViewModels(arguments: Bundle?) {
        val args: MatchesStatusFilterFragmentArgs by navArgs()
        mMatchFilterViewModel.setPreviousMatchFilter(
            args.matchesFilterData
        )
    }

    override fun bindViewModels() {
        observe(mMatchFilterViewModel.matchesFilterSharedFlow) {
            statusesAdapter.replaceItems(it.data?.statusesList ?: arrayListOf())
        }

        observe(mMatchFilterViewModel.matchesFilterAppliedSharedFlow) {
           if(it.isSuccess()){
               setFragmentResult(
                   Constants.FragmentListeners.STATUS_KEY, bundleOf(
                       STATUS_BUNDLE to it.data
                   )
               )
               dismiss()
           }else{
               showMessage(it.error.alternate(getString(R.string.some_thing_went_wrong)))
           }
        }
    }

    override fun showError(shouldShow: Boolean) {
    }


    override fun onItemClick(item: MatchesStatusFilter, position: Int) {
        mMatchFilterViewModel.setData(item.status)
    }
}