package com.farida.coursework.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainView: MvpView {
    fun hideFavoriteList(isHide: Boolean)
    fun isNetworkConnected(isAvailable: Boolean)
}