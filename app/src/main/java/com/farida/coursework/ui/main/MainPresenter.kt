package com.farida.coursework.ui.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import com.farida.coursework.repository.PostsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class MainPresenter(private val postsRepository: PostsRepository) : MvpPresenter<MainView>() {

    private val subscriptions = CompositeDisposable()

    fun loadLikePosts() {
        val subscribe = postsRepository.observeLikePosts()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isNullOrEmpty()) {
                    viewState.hideFavoriteList(false)
                } else {
                    viewState.hideFavoriteList(true)
                }
            }
        subscriptions.add(subscribe)
    }

    fun registerNetworkCallback(context: Context) {
        if (SDK_INT >= Build.VERSION_CODES.N) {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkRequestBuilder =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build()
            connectivityManager.registerDefaultNetworkCallback(object :
                ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    viewState.isNetworkConnected(true)
                }

                override fun onLost(network: Network) {
                    connectivityManager.requestNetwork(
                        networkRequestBuilder,
                        object : ConnectivityManager.NetworkCallback() {
                            override fun onAvailable(network: Network) {
                                viewState.isNetworkConnected(true)
                            }

                            override fun onLost(network: Network) {
                                viewState.isNetworkConnected(false)
                            }
                        })
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}