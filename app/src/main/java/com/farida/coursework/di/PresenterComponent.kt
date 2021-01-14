package com.farida.coursework.di

import com.farida.coursework.ui.likePostsList.LikePostsListFragment
import com.farida.coursework.ui.main.MainActivity
import com.farida.coursework.ui.post.PostFragment
import com.farida.coursework.ui.postsList.PostsListFragment
import com.farida.coursework.ui.profile.ProfileFragment
import dagger.Component

@Component(dependencies = [ApplicationComponent::class], modules = [PresenterModule::class])
@PostsScope
interface PresenterComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(likePostsListFragment: LikePostsListFragment)

    fun inject(postsListFragment: PostsListFragment)

    fun inject(profileFragment: ProfileFragment)

    fun inject(postFragment: PostFragment)
}