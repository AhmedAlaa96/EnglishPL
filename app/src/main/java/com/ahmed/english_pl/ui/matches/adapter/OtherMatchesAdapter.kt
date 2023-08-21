package com.ahmed.english_pl.ui.matches.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.MatchesModel
import com.ahmed.english_pl.databinding.ItemMatchBinding
import com.ahmed.english_pl.databinding.ItemMatchHorizontalBinding
import com.ahmed.english_pl.databinding.ItemMatchVerticalBinding
import com.ahmed.english_pl.ui.base.BaseAdapter
import com.ahmed.english_pl.ui.base.ListItemClickListener

class OtherMatchesAdapter(
    private val mContext: Context,
    private val mMatchItemClickListener: ListItemClickListener<Match>? = null
) : BaseAdapter<Match, MatchVerticalItemHolder>(itemClickListener = mMatchItemClickListener) {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MatchVerticalItemHolder {
        binding = ItemMatchVerticalBinding.inflate(
            LayoutInflater.from(mContext),
            viewGroup,
            false
        )
        return MatchVerticalItemHolder(
            binding as ItemMatchVerticalBinding,
            mMatchItemClickListener
        )
    }

    override fun clearViewBinding() {
        binding = null
    }
}