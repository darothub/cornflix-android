package com.darotapp.cornflix.adapters.utils

import androidx.recyclerview.widget.DiffUtil
import com.darotapp.cornflix.data.local.database.MovieEntity

class MovieDiffUtilCallBack(
    private val oldList: List<MovieEntity>,
    private val newList: List<MovieEntity>
):DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition == newItemPosition
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}