package com.ahmed.english_pl.ui.matches.status_filter.adapter

import androidx.core.view.isVisible
import com.ahmed.english_pl.data.models.MatchesStatusFilter
import com.ahmed.english_pl.databinding.ItemStatusBinding
import com.ahmed.english_pl.ui.base.BaseViewHolder
import com.ahmed.english_pl.ui.base.ListItemClickListener
import com.ahmed.english_pl.utils.alternate

class StatusItemHolder(
    private val binding: ItemStatusBinding,
    private val mStatusItemClickListener: ListItemClickListener<MatchesStatusFilter>? = null
) :
    BaseViewHolder<MatchesStatusFilter>(binding, mStatusItemClickListener) {
    override fun bind(item: MatchesStatusFilter) {
        binding.txtStatus.text = item.status.alternate()
        binding.imgCheck.isVisible = item.isSelected
        itemView.setOnClickListener {
            item.isSelected = true
            mStatusItemClickListener?.onItemClick(item, adapterPosition)
        }
    }

}