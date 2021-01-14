package com.farida.coursework.di

import com.farida.coursework.MyApplication
import com.farida.coursework.dao.PostsDao
import com.farida.coursework.repository.PostsRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RepositoryModule::class, VkServiceModule::class, DatabaseModule::class])
interface ApplicationComponent {
    fun dao(): PostsDao
    fun repo(): PostsRepository
    fun inject(myApplication: MyApplication)
}
