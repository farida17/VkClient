package com.farida.coursework.remote

import com.farida.coursework.model.CommentResponseContainer
import com.farida.coursework.model.GroupResponseContainer
import com.farida.coursework.model.ResponseContainer
import com.farida.coursework.model.UserResponseContainer
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class VkRepository @Inject constructor(private val vkService: VkService) {

    fun getPosts(): Single<ResponseContainer> = vkService.getPosts()

    fun getComments(ownerId: Long, postId: Long): Single<CommentResponseContainer> = vkService.getComments(ownerId, postId)

    fun getGroupById(groupId: Long): Single<GroupResponseContainer> = vkService.getGroupById(groupId)

    fun addLike(postId: Long, ownerId: Long): Completable = vkService.addLike(postId, ownerId)

    fun deleteLike(postId: Long, ownerId: Long): Completable = vkService.deleteLike(postId, ownerId)

    fun hidePost(postId: Long, ownerId: Long): Completable = vkService.hidePost(postId, ownerId)

    fun getUser(): Single<UserResponseContainer> = vkService.getUser()

    fun createPost(message: String): Completable = vkService.createPost(message)

    fun getOwnPosts(): Single<ResponseContainer> = vkService.getOwnPosts()

    fun createComment(ownerId: Long, postId: Long, message: String): Completable = vkService.createComment(ownerId, postId, message)
}