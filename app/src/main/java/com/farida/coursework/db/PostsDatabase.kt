package com.farida.coursework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.farida.coursework.dao.PostsDao
import com.farida.coursework.model.Post

@Database(entities = [Post::class], version = 15, exportSchema = false)
@TypeConverters(PostConverter::class)
abstract class PostsDatabase: RoomDatabase() {

    abstract fun postsDao(): PostsDao
}