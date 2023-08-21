package com.ahmed.english_pl.ui.base

import androidx.recyclerview.widget.LinearLayoutManager

interface ListItemClickListener<in Item> {
    fun onItemClick(item: Item, position: Int)

}
