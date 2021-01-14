package com.farida.coursework.di

import com.farida.coursework.dao.PostsDao
import com.farida.coursework.remote.VkRepository
import com.farida.coursework.repository.PostsRepository
import com.farida.coursework.repository.PostsRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class RepositoryModule {

    @Provides
    fun bindsPostsRepository(postsDao: PostsDao, vkRepository: VkRepository): PostsRepository = PostsRepositoryImpl(postsDao, vkRepository)
}