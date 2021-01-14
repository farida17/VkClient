package com.farida.coursework.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.farida.coursework.db.PostConverter
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import org.joda.time.DateTimeZone
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

data class ResponseContainer(
    @SerializedName("response")
    val response: PostResponse
)

data class PostResponse(
    @SerializedName("items")
    val items: List<Post>,
    @SerializedName("groups")
    val groups: List<Group>,
    @SerializedName("profiles")
    val profiles: List<Profile>
)

data class Profile(
    @SerializedName("id")
    val profileId: Long,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("photo_100")
    val profilePhoto: String
)

@Parcelize
data class Photo(
    @SerializedName("sizes")
    val sizes: List<Sizes>?
) : Parcelable

@Parcelize
data class Sizes(
    @SerializedName("url")
    val url: String?
): Parcelable

@Parcelize
data class Attachment(
    @SerializedName("photo")
    val photo: Photo?
) : Parcelable

@Parcelize
data class Like(
    @SerializedName("user_likes")
    var likesCount: Int?
): Parcelable

@Parcelize
@Entity(tableName = "post")
@TypeConverters(PostConverter::class)
data class Post (
    @SerializedName("source_id")
    val sourceId: Long,
    @SerializedName("from_id")
    val fromId: Long,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val text: String,
    @SerializedName("attachments")
    val attachments: List<Attachment>?,
    @SerializedName("likes")
    var likes: Like?,
    @SerializedName("post_id")
    @PrimaryKey(autoGenerate = true)
    val postId: Long,
    var groupName: String,
    var groupAvatar: String
) : Parcelable {
    val photo
        get() = attachments?.firstOrNull()?.photo?.sizes?.lastOrNull()?.url

    val localDate: LocalDate
        get() = LocalDate(TimeUnit.SECONDS.toMillis(date), DateTimeZone.getDefault())

    val localDateTime: LocalDateTime
        get() = LocalDateTime(TimeUnit.SECONDS.toMillis(date))

    var isLiked: Boolean
        get() = likes?.likesCount == 1
        set(value) {
            likes = Like(if (value) 1 else 0)
        }

    constructor(
        sourceId: Long,
        postId: Long,
        fromId: Long,
        date: Long,
        nameGroup: String,
        groupAvatar: String,
        text: String,
        attachments: List<Attachment>?,
        likes: Like?
    ): this(sourceId, fromId,  date, text, attachments, likes, postId, nameGroup, groupAvatar)
}

@Parcelize
data class Group(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val groupName: String,
    @SerializedName("photo_50")
    val groupAvatar: String,
): Parcelable