package com.farida.coursework

import android.app.Application
import com.farida.coursework.di.*

class MyApplication: Application() {

    lateinit var applicationComponent: ApplicationComponent

    var presenterComponent: PresenterComponent? = null

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

    }
    fun addPostsComponent() {
        presenterComponent = DaggerPresenterComponent.builder().applicationComponent(applicationComponent).build()
    }
    fun clearPostComponent() {
        presenterComponent = null
    }
}