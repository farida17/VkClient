package com.farida.coursework.db

import androidx.room.TypeConverter
import com.farida.coursework.model.Attachment
import com.farida.coursework.model.Like
import com.farida.coursework.model.Photo
import com.farida.coursework.model.Sizes

class PostConverter {

    @TypeConverter
    fun toPhoto(attachments: List<Attachment>?): String? = attachments?.firstOrNull()?.photo?.sizes?.lastOrNull()?.url

    @TypeConverter
    fun fromPhoto(url: String?): List<Attachment> = listOf(Attachment(Photo(listOf(Sizes(url)))))

    @TypeConverter
    fun toLikeState(likes: Like?): Boolean = likes?.likesCount == 1

    @TypeConverter
    fun fromLikeState(isLiked: Boolean): Like = Like(if (isLiked) 1 else 0)
}