package com.farida.coursework.ui.profile

import com.farida.coursework.R
import com.farida.coursework.repository.PostsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class ProfilePresenter (private val postsRepository: PostsRepository): MvpPresenter<ProfileView>(), OnCreatePostButtonListener {

    private val subscriptions = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadUser()
        loadOwnPosts()
    }

    private fun loadUser() {
        postsRepository.getUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = viewState::setUser, onError = Throwable::printStackTrace)
            .addTo(subscriptions)
    }

    private fun loadOwnPosts() {
        val subscribe = postsRepository.getOwnPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onSuccess = {
                viewState.setOwnPosts(it)
            }, onError = {
                viewState.showErrorMessage(R.string.error_load_posts)
            })
        subscriptions.add(subscribe)
    }

    override fun onCreatePostButtonClick(message: String) {
        val subscribe = postsRepository.createPost(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
                loadOwnPosts()
            }, onError = {
                viewState.showErrorMessage(R.string.error_create_post)
            })
        subscriptions.add(subscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}