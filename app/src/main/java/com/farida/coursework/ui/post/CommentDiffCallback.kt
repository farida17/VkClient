package com.farida.coursework.ui.post

import androidx.recyclerview.widget.DiffUtil
import com.farida.coursework.model.Comment

class CommentDiffCallback(private  val oldList: List<Comment>, private  val newList: List<Comment>): DiffUtil.Callback() {

    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos].commentId == newList[newPos].commentId
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
}