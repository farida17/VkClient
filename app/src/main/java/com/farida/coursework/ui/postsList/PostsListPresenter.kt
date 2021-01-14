package com.farida.coursework.ui.postsList

import com.farida.coursework.R
import com.farida.coursework.model.Post
import com.farida.coursework.repository.PostsRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter

@InjectViewState
class PostsListPresenter(private val postsRepository: PostsRepository): MvpPresenter<PostsListView>(), OnPostLikeListener {

    private val subscriptions = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        loadDbPosts()
    }

    fun loadDbPosts() {
        val subscribe = postsRepository.getDbPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onSuccess = {
                if (it.isEmpty()) {
                    loadVkPosts()
                } else {
                    viewState.setPostsList(it)
                    loadVkPosts()
                }
            }, onError = {
                it.printStackTrace()
                viewState.showErrorFragment()
            })
        subscriptions.add(subscribe)
    }

    private fun loadVkPosts() {
        val subscribe = postsRepository.getVkPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onSuccess = {
                updatedDbPosts()
            }, onError = {
                it.printStackTrace()
                viewState.showErrorFragment()
            })
        subscriptions.add(subscribe)
    }

    private fun updatedDbPosts() {
        val subscribe = postsRepository.getDbPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onSuccess = {
                viewState.setPostsList(it)
            }, onError = {
                viewState.showErrorFragment()
            })
        subscriptions.add(subscribe)
    }

    override fun onSendLikePost(post: Post) {
        val subscribe = postsRepository.setLikePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
                insertPostDb(post)
            }, onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    override fun onSendDislikePost(post: Post) {
        val subscribe = postsRepository.dislikePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
               insertPostDb(post)
            }, onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    private fun insertPostDb(post: Post) {
        val subscribe = postsRepository.insertPost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
                updatedDbPosts()
            }, onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    override fun onSendDislikeAndHidePost(post: Post) {
        onSendDislikePostVk(post)
        val subscribe = postsRepository.hidePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
                deletePostFromDb(post)
            }, onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    private fun deletePostFromDb(post: Post) {
        val subscribe = postsRepository.deletePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onComplete = {
                updatedDbPosts()
            }, onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    private fun onSendDislikePostVk(post: Post) {
        val subscribe = postsRepository.dislikePost(post)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy (onError = {
                viewState.showErrorMessage(R.string.error_message)
            })
        subscriptions.add(subscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}