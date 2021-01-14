package com.farida.coursework.ui.postsList

import com.farida.coursework.model.Post

interface OnPostLikeListener {
    fun onSendLikePost(post: Post)
    fun onSendDislikePost(post: Post)
    fun onSendDislikeAndHidePost(post: Post)
}
