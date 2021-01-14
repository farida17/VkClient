package com.farida.coursework.ui.postsList

import com.farida.coursework.model.Post
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface PostsListView: MvpView {
    fun setPostsList(posts: List<Post>)
    fun showErrorFragment()
    fun showErrorMessage(textResource: Int)
}