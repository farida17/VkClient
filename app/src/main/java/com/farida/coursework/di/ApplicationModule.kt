package com.farida.coursework.di

import android.content.Context
import androidx.room.Room
import com.farida.coursework.MyApplication
import com.farida.coursework.db.PostsDatabase
import com.farida.coursework.remote.VkRepository
import com.farida.coursework.remote.VkService
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: MyApplication) {

    @Provides
    @Singleton
    fun provideAppContext(): Context = application

    @Provides
    @Singleton
    fun provideDatabase() = Room.databaseBuilder(provideAppContext(), PostsDatabase::class.java, "post_db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun providesVkRepository(vkService: VkService): VkRepository {
        return VkRepository(vkService)
    }
}