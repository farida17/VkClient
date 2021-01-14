package com.farida.coursework.ui.post

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
class PostPresenter(private val postsRepository: PostsRepository): MvpPresenter<PostView>() {

    private val subscriptions = CompositeDisposable()

    fun loadPost(post: Post) {
        viewState.setOpenPost(post)
        loadComments(post.sourceId, post.postId)
    }

    private fun loadComments(ownerId: Long, postId: Long) {
        val subscribe = postsRepository.getComments(ownerId, postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                viewState.setComments(it)
            }, onError = {
                it.printStackTrace()
            })
        subscriptions.add(subscribe)
    }

    fun onCreateComment(ownerId: Long, postId: Long, message: String) {
        val subscribe = postsRepository.createComment(ownerId, postId, message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onComplete = {
                loadComments(ownerId, postId)
            }, onError = {
                it.printStackTrace()
                viewState.showErrorMessage(R.string.error_create_comment)
            })
        subscriptions.add(subscribe)
    }

    override fun onDestroy() {
        super.onDestroy()
        subscriptions.dispose()
    }
}