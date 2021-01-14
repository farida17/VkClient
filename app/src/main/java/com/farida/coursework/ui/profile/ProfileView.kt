package com.farida.coursework.ui.profile

import com.farida.coursework.model.Post
import com.farida.coursework.model.UserResponse
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface ProfileView: MvpView {
    fun setUser(userResponse: UserResponse)
    fun setOwnPosts(posts: List<Post>)
    fun showErrorMessage(textResource: Int)
}