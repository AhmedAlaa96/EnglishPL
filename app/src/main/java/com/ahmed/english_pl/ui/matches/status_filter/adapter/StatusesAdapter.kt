package com.ahmed.english_pl.ui.matches.status_filter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ahmed.english_pl.data.models.MatchesStatusFilter
import com.ahmed.english_pl.databinding.ItemMatchHorizontalBinding
import com.ahmed.english_pl.databinding.ItemStatusBinding
import com.ahmed.english_pl.ui.base.BaseAdapter
import com.ahmed.english_pl.ui.base.ListItemClickListener

class StatusesAdapter(
    private val mContext: Context,
    private val mStatusItemClickListener: ListItemClickListener<MatchesStatusFilter>? = null
) : BaseAdapter<MatchesStatusFilter, StatusItemHolder>(itemClickListener = mStatusItemClickListener) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StatusItemHolder {
        binding = ItemStatusBinding.inflate(
            LayoutInflater.from(mContext),
            viewGroup,
            false
        )
        return StatusItemHolder(
            binding as ItemStatusBinding,
            mStatusItemClickListener
        )
    }

    override fun clearViewBinding() {
        binding = null
    }
}