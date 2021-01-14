package com.farida.coursework.ui.post

import com.farida.coursework.model.Comment
import com.farida.coursework.model.Post
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface PostView: MvpView {
    fun setLoading(isLoading: Boolean)
    fun setOpenPost(post: Post)
    fun setComments(comments: List<Comment>)
    fun showErrorFragment()
    fun showErrorMessage(textResource: Int)
}