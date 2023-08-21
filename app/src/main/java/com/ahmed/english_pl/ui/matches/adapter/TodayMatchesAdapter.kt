package com.ahmed.english_pl.ui.matches.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.databinding.ItemMatchHorizontalBinding
import com.ahmed.english_pl.ui.base.BaseAdapter
import com.ahmed.english_pl.ui.base.ListItemClickListener

class TodayMatchesAdapter(
    private val mContext: Context,
    private val mMatchItemClickListener: ListItemClickListener<Match>? = null
) : BaseAdapter<Match, MatchHorizontalItemHolder>(itemClickListener = mMatchItemClickListener) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MatchHorizontalItemHolder {
        binding = ItemMatchHorizontalBinding.inflate(
            LayoutInflater.from(mContext),
            viewGroup,
            false
        )
        return MatchHorizontalItemHolder(
            binding as ItemMatchHorizontalBinding,
            mMatchItemClickListener
        )
    }

    override fun clearViewBinding() {
        binding = null
    }
}