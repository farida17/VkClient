package com.farida.coursework.ui.base

import androidx.recyclerview.widget.DiffUtil
import com.farida.coursework.model.Post

class BaseDiffCallback(private  val oldList: List<Post>, private  val newList: List<Post>): DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos].postId == newList[newPos].postId
        }

        override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
            return oldList[oldPos] == newList[newPos]
        }
}