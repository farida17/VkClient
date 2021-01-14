package com.farida.coursework.di

import com.farida.coursework.repository.PostsRepository
import com.farida.coursework.ui.likePostsList.LikePostsListPresenter
import com.farida.coursework.ui.main.MainPresenter
import com.farida.coursework.ui.post.PostPresenter
import com.farida.coursework.ui.postsList.PostsListPresenter
import com.farida.coursework.ui.profile.ProfilePresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun provideMainPresenter(postsRepository: PostsRepository): MainPresenter = MainPresenter(postsRepository)

    @Provides
    fun providePostPresenter(postsRepository: PostsRepository): PostPresenter = PostPresenter(postsRepository)

    @Provides
    fun providePostsListPresenter(postsRepository: PostsRepository): PostsListPresenter = PostsListPresenter(postsRepository)

    @Provides
    fun provideLikePostsPresenter(postsRepository: PostsRepository): LikePostsListPresenter = LikePostsListPresenter(postsRepository)

    @Provides
    fun provideProfilePresenter(postsRepository: PostsRepository): ProfilePresenter = ProfilePresenter(postsRepository)
}