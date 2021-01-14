package com.farida.coursework.repository

import com.farida.coursework.dao.PostsDao
import com.farida.coursework.model.Comment
import com.farida.coursework.model.Post
import com.farida.coursework.model.UserResponse
import com.farida.coursework.remote.VkRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import kotlin.math.abs

class PostsRepositoryImpl(private val postsDao: PostsDao, private val vkRepository: VkRepository): PostsRepository {

    override fun getVkPosts(): Single<List<Post>> {

        return vkRepository.getPosts()
            .map { responseContainer ->
                val postsById = responseContainer.response.items.groupBy { post -> abs(post.sourceId) }
                responseContainer.response.groups.forEach { group ->
                    postsById[group.id]?.forEach { post -> 
                        post.groupName = group.groupName
                        post.groupAvatar = group.groupAvatar
                    }
                }
                postsDao.deleteAllPosts()
                postsDao.insertAllPosts(responseContainer.response.items)
                responseContainer.response.items
            }
    }

    override fun getDbPosts(): Single<List<Post>> {
        return postsDao.getAllPosts()
    }

    override fun dislikePost(post: Post): Completable {
        return vkRepository.deleteLike(post.postId, post.sourceId)
    }

    override fun insertPost(post: Post): Completable {
        return postsDao.insertPost(post)
    }

    override fun getLikePosts(): Single<List<Post>> {
        return postsDao.getLikePosts(isLiked = true)
    }

    override fun observeLikePosts(): Observable<List<Post>> {
        return postsDao.observeLikePosts(isLiked = true)
    }

    override fun hidePost(post: Post): Completable {
        return vkRepository.hidePost(post.postId, post.sourceId)
    }

    override fun deletePost(post: Post): Completable {
        return postsDao.deletePost(post)
    }

    override fun setLikePost(post: Post): Completable {
        return vkRepository.addLike(post.postId, post.sourceId)
    }

    override fun getUser(): Single<UserResponse> {
        return vkRepository.getUser()
            .map { it.response.first() }
            .flatMap { userResponse ->
                val groupIds = userResponse.career.orEmpty()
                    .filter { it.company.isNullOrBlank() }
                    .map { it.groupId }
                    .toList()

                if (groupIds.isNotEmpty()) {
                    Observable.fromIterable(groupIds)
                        .flatMapSingle { groupId ->
                            getGroupById(groupId)
                                .map { groupName -> groupId to groupName }
                        }
                        .toList()
                        .map { idToNameGroups ->
                            val map: Map<Long, String> = idToNameGroups.associate { it }
                            userResponse.copy(career = userResponse.career?.map { career ->
                                map[career.groupId]?.let {
                                    career.copy(company = it)
                                } ?: career}
                            )
                        }
                } else {
                    Single.just(userResponse)
                }
            }
    }

    private fun getGroupById(groupId: Long): Single<String> {
        return vkRepository.getGroupById(groupId)
            .map {
                it.groupResponse.first().groupName
            }
    }

    override fun createPost(message: String): Completable {
        return vkRepository.createPost(message)
    }

    override fun getOwnPosts(): Single<List<Post>> {
        return vkRepository.getOwnPosts()
            .map { responseContainer ->
                val filteredPostsList = responseContainer.response.items.filter { item ->
                    !item.photo.isNullOrBlank() || item.text.isNotBlank()
                }

                val postsByFromId = filteredPostsList.groupBy { it.fromId }
                responseContainer.response.profiles.forEach { profile ->
                    postsByFromId[profile.profileId]?.forEach { post ->
                        post.groupName = profile.firstName + " " + profile.lastName
                        post.groupAvatar = profile.profilePhoto
                    }
                }
                filteredPostsList
            }
    }

    override fun getComments(ownerId: Long, postId: Long): Single<List<Comment>> {
        return vkRepository.getComments(ownerId, postId)
            .map { commentResponseContainer ->
                val commentsByFromId = commentResponseContainer.response.items.groupBy { abs(it.fromId) }
                commentResponseContainer.response.profiles.forEach { profile ->
                    commentsByFromId[profile.profileId]?.forEach { comment ->
                        comment.groupName = profile.firstName + " " + profile.lastName
                        comment.groupAvatar = profile.profilePhoto
                    }
                }
                commentResponseContainer.response.groups.forEach { groups ->
                    commentsByFromId[groups.id]?.forEach { comment ->
                        comment.groupName = groups.groupName
                        comment.groupAvatar = groups.groupAvatar
                    }
                }
                commentResponseContainer.response.items
            }
    }

    override fun createComment(ownerId: Long, postId: Long, message: String): Completable {
        return vkRepository.createComment(ownerId, postId, message)
    }
}