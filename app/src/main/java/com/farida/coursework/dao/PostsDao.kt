package com.farida.coursework.dao

import androidx.room.*
import com.farida.coursework.model.Post
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface PostsDao {

    @Query("SELECT * FROM post ORDER BY date DESC")
    fun getAllPosts(): Single<List<Post>>

    @Query("SELECT * FROM post WHERE likes = :isLiked ")
    fun getLikePosts(isLiked: Boolean): Single<List<Post>>

    @Query("SELECT * FROM post WHERE likes = :isLiked ")
    fun observeLikePosts(isLiked: Boolean): Observable<List<Post>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPost(post: Post): Completable

    @Delete(entity = Post::class)
    fun deletePost(post: Post): Completable

    @Query("DELETE from post")
    fun deleteAllPosts()
}