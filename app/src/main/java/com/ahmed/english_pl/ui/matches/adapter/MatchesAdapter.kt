package com.ahmed.english_pl.ui.matches.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchesModel
import com.ahmed.english_pl.databinding.ItemMatchBinding
import com.ahmed.english_pl.ui.base.BaseAdapter
import com.ahmed.english_pl.ui.base.ListItemClickListener

class MatchesAdapter(
    private val mContext: Context,
    private val mMatchesItemClickListener: ListItemClickListener<MatchesModel>? = null,
    private val mMatchItemClickListener: ListItemClickListener<Match>? = null
) : BaseAdapter<MatchesModel, MatchItemHolder>(itemClickListener = mMatchesItemClickListener) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MatchItemHolder {
        binding = ItemMatchBinding.inflate(
            LayoutInflater.from(mContext),
            viewGroup,
            false
        )
        return MatchItemHolder(
            binding as ItemMatchBinding,
            mContext,
            mMatchesItemClickListener,
            mMatchItemClickListener
        )
    }

    override fun clearViewBinding() {
        binding = null
    }
}