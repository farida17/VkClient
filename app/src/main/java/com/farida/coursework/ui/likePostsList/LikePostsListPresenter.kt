package com.farida.coursework.ui.likePostsList

import com.farida.coursework.R
import com.farida.coursework.repository.PostsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class LikePostsListPresenter(var postsRepository: PostsRepository): MvpPresenter<LikePostView>() {

    private val subscriptions = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadLikePosts()
    }

    private fun loadLikePosts() {
        val subscribe = postsRepository.getLikePosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onSuccess =  {
                viewState.setLikePostsList(it)
            }, onError = {
                viewState.setErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}