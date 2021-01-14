package com.farida.coursework.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.farida.coursework.MyApplication
import com.farida.coursework.R
import com.farida.coursework.R.layout
import com.farida.coursework.model.Post
import com.farida.coursework.remote.TokenHolder.tokenAccess
import com.farida.coursework.ui.authorization.AuthorizationFragment
import com.farida.coursework.ui.likePostsList.LikePostsListFragment
import com.farida.coursework.ui.post.PostFragment
import com.farida.coursework.ui.postsList.OnBottomNavigationVisibilityListener
import com.farida.coursework.ui.postsList.OnOpenPostClickListener
import com.farida.coursework.ui.postsList.PostsListFragment
import com.farida.coursework.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : MvpAppCompatActivity(), OnOpenPostClickListener, OnBottomNavigationVisibilityListener, MainView {

    @Inject
    lateinit var mainPresenterProvider: Provider<MainPresenter>

    @InjectPresenter
    lateinit var mainPresenter: MainPresenter

    @ProvidePresenter
    fun providePresenter(): MainPresenter {
        return mainPresenterProvider.get()
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.postsListFragmentContainer, ProfileFragment())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.newsList -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.postsListFragmentContainer, PostsListFragment())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.favoriteList -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.postsListFragmentContainer, LikePostsListFragment())
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).addPostsComponent()
        (applicationContext as MyApplication).presenterComponent?.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)
        mainPresenter.registerNetworkCallback(this)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.postsListFragmentContainer, AuthorizationFragment())
                .commit()
        }
        bottom_navigation.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        mainPresenter.loadLikePosts()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val callback = object: VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                tokenAccess = token.accessToken
                supportFragmentManager.beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.postsListFragmentContainer, PostsListFragment())
                    .commitAllowingStateLoss()
            }
            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(this@MainActivity, R.string.login_error_message, Toast.LENGTH_SHORT).show()
            }
        }
        if (data == null || ! VK.onActivityResult(requestCode, resultCode, data, callback)) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSetBottomNavigationVisibility(visibility: Int) {
        bottom_navigation.visibility = visibility
    }

    override fun hideFavoriteList(isHide: Boolean) {
        bottom_navigation.menu.findItem(R.id.favoriteList).isVisible = isHide
    }

    override fun isNetworkConnected(isAvailable: Boolean) {
        if (!isAvailable) {
            Toast.makeText(this, R.string.network_error_message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onSendPost(post: Post) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.postsListFragmentContainer, PostFragment.newInstance(post))
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        (applicationContext as MyApplication).clearPostComponent()
    }
}