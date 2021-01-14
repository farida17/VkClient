package com.farida.coursework.ui.likePostsList

import com.farida.coursework.model.Post
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface LikePostView: MvpView {
    fun setLikePostsList(likePosts: List<Post>)
    fun setErrorMessage(textResource: Int)
}