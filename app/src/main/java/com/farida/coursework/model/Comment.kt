package com.farida.coursework.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class CommentResponseContainer(
    @SerializedName("response")
    val response: CommentResponse
)

data class CommentResponse(
    @SerializedName("items")
    val items: List<Comment>,
    @SerializedName("groups")
    val groups: List<Group>,
    @SerializedName("profiles")
    val profiles: List<Profile>
)

data class Comment(
    @SerializedName("source_id")
    val sourceId: Long,
    @SerializedName("from_id")
    val fromId: Long,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val text: String?,
    @SerializedName("attachments")
    val attachments: List<Attachment>?,
    @SerializedName("likes")
    var likes: Like?,
    @SerializedName("post_id")
    @PrimaryKey(autoGenerate = true)
    val commentId: Long,
    var groupName: String,
    var groupAvatar: String,
    @SerializedName("deleted")
    val isDeleted: Boolean
)