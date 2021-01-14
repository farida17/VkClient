package com.farida.coursework.model

import com.google.gson.annotations.SerializedName

data class UserResponseContainer(
    @SerializedName("response")
    val response: List<UserResponse>
)

data class GroupResponseContainer(
    @SerializedName("response")
    val groupResponse: List<Group>
)

data class City(
    @SerializedName("title")
    val cityTitle: String?
)

data class Country(
    @SerializedName("title")
    val countryTitle: String?
)

data class Career(
    @SerializedName("group_id")
    val groupId: Long,
    @SerializedName("company")
    var company: String?,
    @SerializedName("from")
    val from: Int,
    @SerializedName("until")
    val until: Int,
    @SerializedName("position")
    val position: String?
)

data class LastSeen(
    @SerializedName("time")
    val time: Long?
)

data class UserResponse(
    @SerializedName("id")
    val userId: Long,
    @SerializedName("domain")
    val domain: String,
    @SerializedName("photo_200")
    val photo: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("about")
    val about: String?,
    @SerializedName(" last_seen")
    val lastSeen: LastSeen?,
    @SerializedName("bdate")
    val bDate: String?,
    @SerializedName("city")
    val city: City?,
    @SerializedName("country")
    val country: Country?,
    @SerializedName("university_name")
    val universityName: String?,
    @SerializedName("faculty_name")
    val facultyName: String?,
    @SerializedName("career")
    val career: List<Career>?,
    @SerializedName("graduation")
    val graduation: Int?,
    @SerializedName("followers_count")
    val followersCount: Int
)