package com.farida.coursework.repository

import com.farida.coursework.model.Comment
import com.farida.coursework.model.Post
import com.farida.coursework.model.UserResponse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface PostsRepository {
    fun getVkPosts(): Single<List<Post>>
    fun getDbPosts(): Single<List<Post>>
    fun getLikePosts(): Single<List<Post>>
    fun observeLikePosts(): Observable<List<Post>>
    fun hidePost(post: Post): Completable
    fun deletePost(post: Post): Completable
    fun dislikePost(post: Post): Completable
    fun insertPost(post: Post): Completable
    fun setLikePost(post: Post): Completable
    fun getUser(): Single<UserResponse>
    fun createPost(message: String): Completable
    fun getOwnPosts(): Single<List<Post>>
    fun getComments(ownerId: Long, postId: Long): Single<List<Comment>>
    fun createComment(ownerId: Long, postId: Long, message: String): Completable
}