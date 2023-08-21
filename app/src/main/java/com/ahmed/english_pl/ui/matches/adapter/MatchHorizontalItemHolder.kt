package com.ahmed.english_pl.ui.matches.adapter

import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.ahmed.english_pl.R
import com.ahmed.english_pl.data.models.dto.Match
import com.ahmed.english_pl.data.models.dto.Score
import com.ahmed.english_pl.databinding.ItemMatchHorizontalBinding
import com.ahmed.english_pl.databinding.ItemMatchVerticalBinding
import com.ahmed.english_pl.ui.base.BaseViewHolder
import com.ahmed.english_pl.ui.base.ListItemClickListener
import com.ahmed.english_pl.utils.DateTimeHelper
import com.ahmed.english_pl.utils.alternate
import java.util.*

class MatchHorizontalItemHolder(
    private val binding: ItemMatchHorizontalBinding,
    private val mMatchItemClickListener: ListItemClickListener<Match>? = null
) :
    BaseViewHolder<Match>(binding, mMatchItemClickListener) {
    override fun bind(item: Match) {
        bindMatchName(item.homeTeam?.name, item.awayTeam?.name)
        bindMatchStatus(item.status)
        bindMatchScore(item.score, item.status, item.utcDate)
        bindMatchIsFavorite(item.isFavorite)
        onMatchClicked(item)
        itemView.isEnabled = false
    }

    private fun bindMatchIsFavorite(isFavorite: Boolean) {
        changeFavoriteIcon(isFavorite)
    }

    private fun onMatchClicked(item: Match) {
        binding.btnFavorite.setOnClickListener {
            item.isFavorite = !item.isFavorite
            changeFavoriteIcon(item.isFavorite)
            mMatchItemClickListener?.onItemClick(item, adapterPosition)
        }
    }

    private fun bindMatchScore(score: Score?, status: String?, utcDate: String?) {
        if (status == "SCHEDULED") {
            binding.txtMatchScore.text = DateTimeHelper.convertDateStringToAnotherFormat(
                utcDate,
                dateFormatter = DateTimeHelper.DATE_FORMAT_E_d_MMM
            )
            return
        }
        binding.txtMatchScore.text =
            if (score?.fullTime?.homeTeam != null && score.fullTime.awayTeam != null)
                "FT: ${score.fullTime.homeTeam} - ${score.fullTime.awayTeam}"
            else if (score?.halfTime?.homeTeam != null && score.halfTime.awayTeam != null)
                "HT: ${score.halfTime.homeTeam} - ${score.halfTime.awayTeam}"
            else {
                "${score?.halfTime?.homeTeam} - ${score?.halfTime?.awayTeam}"
            }

    }

    private fun bindMatchStatus(status: String?) {
        binding.txtMatchStatus.text = status.alternate().lowercase().replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
    }

    private fun changeFavoriteIcon(isFavorite: Boolean) {
        val favoriteDrawable =
            if (isFavorite) R.drawable.ic_favorite_selected else R.drawable.ic_favorite_unselected
        binding.btnFavorite.setImageDrawable(
            ContextCompat.getDrawable(
                binding.btnFavorite.context,
                favoriteDrawable
            )
        )
    }


    private fun bindMatchName(homeName: String?, awayName: String?) {
        binding.txtMatchName.text = getMatchName(homeName, awayName)
    }

    private fun getMatchName(homeName: String?, awayName: String?): String {
        return "${homeName.alternate()} vs ${awayName.alternate()}"
    }
}