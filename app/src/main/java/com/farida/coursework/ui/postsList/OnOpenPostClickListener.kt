package com.farida.coursework.ui.postsList

import com.farida.coursework.model.Post

interface OnOpenPostClickListener {
    fun onSendPost(post: Post)
}