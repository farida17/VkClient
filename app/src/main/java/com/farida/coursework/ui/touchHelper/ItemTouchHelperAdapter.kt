package com.farida.coursework.ui.touchHelper

interface ItemTouchHelperAdapter {
    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean
    fun onHidePost(position: Int)
    fun onLikeButtonClick(position: Int)
}