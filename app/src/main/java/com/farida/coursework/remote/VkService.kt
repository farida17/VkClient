package com.farida.coursework.remote

import com.farida.coursework.model.CommentResponseContainer
import com.farida.coursework.model.GroupResponseContainer
import com.farida.coursework.model.ResponseContainer
import com.farida.coursework.model.UserResponseContainer
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface VkService {

    @GET("newsfeed.get?filters=post&source_ids=groups&fields=id,name,photo_50")
    fun getPosts(): Single<ResponseContainer>

    @GET("users.get?fields=first_name, last_name, bdate, city, country, photo_200, domain, education, last_seen, followers_count, about, career")
    fun getUser(): Single<UserResponseContainer>

    @GET("wall.get?extended=1")
    fun getOwnPosts(): Single<ResponseContainer>

    @GET("groups.getById")
    fun getGroupById(@Query("group_id") groupId: Long): Single<GroupResponseContainer>

    @GET("wall.getComments?extended=1")
    fun getComments(@Query("owner_id") ownerId: Long, @Query("post_id") postId: Long): Single<CommentResponseContainer>

    @POST("likes.add?type=post")
    fun addLike(@Query("item_id") postId: Long, @Query("owner_id") ownerId: Long): Completable

    @POST("likes.delete?type=post")
    fun deleteLike(@Query("item_id") postId: Long, @Query("owner_id") ownerId: Long): Completable

    @POST("newsfeed.ignoreItem?type=wall")
    fun hidePost(@Query("item_id") postId: Long, @Query("owner_id") ownerId: Long): Completable

    @POST("wall.post?")
    fun createPost(@Query("message") message: String): Completable

    @POST("wall.createComment?")
    fun createComment(@Query("owner_id") ownerId: Long, @Query("post_id") postId: Long, @Query("message") message: String): Completable
}