package com.farida.coursework.di

import com.farida.coursework.dao.PostsDao
import com.farida.coursework.db.PostsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesPostsDao(db: PostsDatabase): PostsDao {
        return db.postsDao()
    }
}