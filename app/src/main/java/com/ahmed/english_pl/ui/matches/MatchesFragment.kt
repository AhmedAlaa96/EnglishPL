package com.ahmed.english_pl.ui.matches

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahmed.english_pl.R
import com.ahmed.english_pl.data.models.LoadingModel
import com.ahmed.english_pl.data.models.MatchesFilterData
import com.ahmed.english_pl.data.models.ProgressTypes
import com.ahmed.english_pl.data.models.StatusCode
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchesModel
import com.ahmed.english_pl.databinding.FragmentMatchesBinding
import com.ahmed.english_pl.ui.base.BaseFragment
import com.ahmed.english_pl.ui.base.IToolbar
import com.ahmed.english_pl.ui.base.ListItemClickListener
import com.ahmed.english_pl.ui.matches.adapter.MatchesAdapter
import com.ahmed.english_pl.utils.Constants
import com.ahmed.english_pl.utils.Constants.FragmentListeners.FILTER_BUNDLE
import com.ahmed.english_pl.utils.Constants.FragmentListeners.FILTER_KEY
import com.ahmed.english_pl.utils.DateTimeHelper
import com.ahmed.english_pl.utils.alternate
import com.ahmed.english_pl.utils.observe
import com.ahmed.english_pl.utils.view_state.SaveStateLifecycleObserver
import com.ahmed.english_pl.utils.view_state.ViewStateHelper
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MatchesFragment : BaseFragment<FragmentMatchesBinding>(),
    IToolbar,
    ListItemClickListener<Match> {

    companion object {
        const val TAG = "MatchesFragment"
    }

    private val mMatchesViewModel: MatchesViewModel by viewModels()
    private lateinit var matchesAdapter: MatchesAdapter

    private var menuItem: MenuItem? = null
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentMatchesBinding
        get() = FragmentMatchesBinding::inflate


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        initViewStateLifecycle()
        setFragmentResultListener(FILTER_KEY) { _, bundle ->
            val matchesFilterData = bundle.getParcelable<MatchesFilterData>(FILTER_BUNDLE)
            mMatchesViewModel.setMatchesFilterAndShowData(matchesFilterData ?: MatchesFilterData())
        }
    }

    override fun initViews() {
        initMatchesRecyclerView()
        setToolbar(mContext, getString(R.string.app_name))
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        menuItem = menu.findItem(R.id.btnActionFavorite)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.btnActionFilter) {
            onFilterClicked()
        } else if (item.itemId == R.id.btnActionFavorite) {
            onFavoriteMenuClicked()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onFilterClicked() {
        navigateTo(
            MatchesFragmentDirections.actionMatchesFilterFragment(
                mMatchesViewModel
                    .mMatchesFilterData
            )
        )
    }

    private fun onFavoriteMenuClicked() {
        mMatchesViewModel.getFavoriteClicked(!mMatchesViewModel.isFavoriteList)
        menuItem?.icon = if (mMatchesViewModel.isFavoriteList) {
            ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_selected)
        } else {
            ContextCompat.getDrawable(mContext, R.drawable.ic_favorite_selected_white)
        }

    }

    override fun setListeners() {
        binding.errorLayout.btnRetry.setOnClickListener {
            binding.errorLayout.root.isVisible = false
            binding.viewProgress.root.isVisible = true
            mMatchesViewModel.retryGetMatches()
        }

        binding.swRefresh.setOnRefreshListener {
            mMatchesViewModel.refreshGetMatches()
        }
    }

    override fun bindViewModels() {
        bindLoadingObserver()
        bindErrorObserver()
        bindToastMessageObserver()
        bindGetMatchesObserver()
        mMatchesViewModel.getMatchesResponse()
    }

    private fun initMatchesRecyclerView() {
        matchesAdapter = MatchesAdapter(mContext, mMatchItemClickListener = this)
        val mMatchesLayoutManager = LinearLayoutManager(mContext)
        binding.rvMatchesList.layoutManager = mMatchesLayoutManager
        binding.rvMatchesList.adapter = matchesAdapter
    }

    private fun bindGetMatchesObserver() {
        observe(mMatchesViewModel.matchesResponseSharedFlow) {
            when (it.statusCode) {
                StatusCode.SUCCESS -> {
                    binding.swRefresh.isVisible = true
                    binding.errorLayout.root.isVisible = false
                    if (it.data?.todayMatches.isNullOrEmpty() && it.data?.matches.isNullOrEmpty()) {
                        matchesAdapter.clearItems()
                        binding.errorLayout.root.isVisible = true
                        binding.errorLayout.txtError.text = getString(R.string.no_data)
                        binding.errorLayout.btnRetry.isVisible = false
                        bindGetMatchesObserver()
                    } else {
                        matchesAdapter.clearItems()
                        matchesAdapter.addItem(
                            MatchesModel(it.data?.todayMatches, it.data?.matches)
                        )
                    }
                }
                else -> {
                    binding.swRefresh.isVisible = false
                    binding.errorLayout.root.isVisible = true
                    binding.errorLayout.txtError.text =
                        it.error.alternate(getString(R.string.some_thing_went_wrong))
                    binding.errorLayout.btnRetry.isVisible = true
                }
            }
        }

        observe(mMatchesViewModel.matchesFilterSharedFlow) {
            if (it.isSuccess()) {
                binding.chipGroup.removeAllViews()
                val matchesFilterData = it.data
                if (!matchesFilterData?.fromDate.isNullOrEmpty()) {
                    val chip = Chip(mContext)
                    chip.text = "From: " + DateTimeHelper.checkDate(matchesFilterData?.fromDate)
                    chip.isClickable = true
                    chip.isChecked = true
                    chip.isCheckedIconVisible = true
                    chip.setCloseIconResource(R.drawable.ic_close)
                    chip.setOnClickListener {
                        mMatchesViewModel.setMatchesFilterAndShowData(
                            matchesFilterData?.copy(fromDate = null) ?: MatchesFilterData()
                        )
                        binding.chipGroup.removeView(chip)
                    }
                    binding.chipGroup.addView(chip)
                }
                if (!matchesFilterData?.toDate.isNullOrEmpty()) {
                    val chip = Chip(mContext)
                    chip.text = "To: " + DateTimeHelper.checkDate(matchesFilterData?.toDate)
                    chip.isClickable = true
                    chip.isChecked = true
                    chip.isCheckedIconVisible = true
                    chip.setCloseIconResource(R.drawable.ic_close)
                    chip.setOnClickListener {
                        mMatchesViewModel.setMatchesFilterAndShowData(
                            matchesFilterData?.copy(toDate = null) ?: MatchesFilterData()
                        )
                        binding.chipGroup.removeView(chip)
                    }
                    binding.chipGroup.addView(chip)
                }

                if (!matchesFilterData?.status.isNullOrEmpty() && matchesFilterData?.status != "All") {
                    val chip = Chip(mContext)
                    chip.text = matchesFilterData?.status.alternate()
                    chip.isClickable = true
                    chip.isChecked = true
                    chip.isCheckedIconVisible = true
                    chip.setCloseIconResource(R.drawable.ic_close)
                    chip.setOnClickListener {
                        mMatchesViewModel.setMatchesFilterAndShowData(
                            matchesFilterData?.copy(status = null) ?: MatchesFilterData()
                        )
                        binding.chipGroup.removeView(chip)
                    }
                    binding.chipGroup.addView(chip)
                }
            }
        }
    }

    override fun showError(shouldShow: Boolean) {

    }

    private fun bindLoadingObserver() {
        observe(mMatchesViewModel.loadingObservable) {
            onLoadingObserverRetrieved(it)
        }
    }

    private fun onLoadingObserverRetrieved(loadingModel: LoadingModel) {
        loadingModel.loadingProgressView = binding.viewProgress.loadingIndicator
        loadingModel.pullToRefreshProgressView = binding.swRefresh
        loadingModel.loadingFullProgressView = binding.viewFullProgress.root
        binding.viewProgress.root.isVisible =
            (loadingModel.shouldShow && loadingModel.progressType == ProgressTypes.MAIN_PROGRESS)

        binding.viewFullProgress.root.isVisible =
            (loadingModel.shouldShow && loadingModel.progressType == ProgressTypes.FULL_PROGRESS)
        binding.swRefresh.isRefreshing =
            (loadingModel.shouldShow && loadingModel.progressType == ProgressTypes.PULL_TO_REFRESH_PROGRESS)
    }

    private fun bindErrorObserver() {
        observe(mMatchesViewModel.errorViewObservable) {
            showError(it)
            binding.errorLayout.root.visibility = View.GONE
        }
    }


    private fun bindToastMessageObserver() {
        observe(mMatchesViewModel.showToastObservable) {
            showMessage(it)
        }
    }

    private fun initViewStateLifecycle() {
        val viewStateLifecycleObserver =
            SaveStateLifecycleObserver(this::saveState, this::setViewsTags)
        viewLifecycleOwner.lifecycle.addObserver(viewStateLifecycleObserver)
    }

    private fun saveState() {
    }

    private fun setViewsTags() {
        ViewStateHelper.setViewTag(
            binding.rvMatchesList,
            Constants.ViewsTags.RECYCLER_VIEW_MATCHES
        )
    }


    override fun onItemClick(item: Match, position: Int) {
        mMatchesViewModel.onItemMatchFavoriteClicked(item)
    }


    override var mToolbar: Toolbar?
        get() = binding.viewToolbar.root
        set(_) {}


}
