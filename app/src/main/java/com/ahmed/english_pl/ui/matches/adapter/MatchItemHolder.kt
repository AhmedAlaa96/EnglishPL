package com.ahmed.english_pl.ui.matches.adapter

import android.content.Context
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchesModel
import com.ahmed.english_pl.databinding.ItemMatchBinding
import com.ahmed.english_pl.ui.base.BaseViewHolder
import com.ahmed.english_pl.ui.base.ListItemClickListener
import java.util.ArrayList

class MatchItemHolder(
    private val binding: ItemMatchBinding,
    private val mContext: Context,
    listItemClickListener: ListItemClickListener<MatchesModel>? = null,
    private val mMatchItemClickListener: ListItemClickListener<Match>? = null
) : BaseViewHolder<MatchesModel>(binding, listItemClickListener) {
    override fun bind(item: MatchesModel) {
        bindTodayMatches(item.todayMatches ?: arrayListOf())
        bindOtherMatches(item.otherMatches ?: arrayListOf())
    }

    private fun bindTodayMatches(todayMatches: ArrayList<Match>) {
        if (todayMatches.isEmpty()) {
            binding.rvMatchesTodayList.isVisible = false
        } else {
            binding.rvMatchesTodayList.isVisible = true
            val todayMatchesAdapter =
                TodayMatchesAdapter(mContext, mMatchItemClickListener)
            binding.rvMatchesTodayList.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvMatchesTodayList.adapter = todayMatchesAdapter
            todayMatchesAdapter.replaceItems(todayMatches)

            binding.rvMatchesTodayList.addOnItemTouchListener(getOnItemTouchListener())
        }
    }

    private fun bindOtherMatches(otherMatches: ArrayList<Match>) {
        if (otherMatches.isEmpty()) {
            binding.rvMatchesOtherList.isVisible = false
        } else {
            binding.rvMatchesOtherList.isVisible = true
            val otherMatchesAdapter =
                OtherMatchesAdapter(mContext, mMatchItemClickListener)
            binding.rvMatchesOtherList.layoutManager =
                LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
            binding.rvMatchesOtherList.adapter = otherMatchesAdapter
            otherMatchesAdapter.replaceItems(otherMatches)
            binding.rvMatchesOtherList.addOnItemTouchListener(getOnItemTouchListener())
        }
    }

    private fun getOnItemTouchListener(): RecyclerView.OnItemTouchListener {
        return object : RecyclerView.OnItemTouchListener {
            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                when (e.action) {
                    MotionEvent.ACTION_DOWN -> rv.parent?.requestDisallowInterceptTouchEvent(true)
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> rv.parent?.requestDisallowInterceptTouchEvent(
                        false
                    )
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
        }
    }

}