package com.farida.coursework.ui.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farida.coursework.R
import com.farida.coursework.model.Post
import com.farida.coursework.model.UserResponse
import com.farida.coursework.ui.base.BaseDiffCallback
import com.farida.coursework.util.Utils.dateTimeToString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_view_image_post.view.*
import kotlinx.android.synthetic.main.list_view_post.view.*
import kotlinx.android.synthetic.main.profile_layout.view.*
import kotlinx.android.synthetic.main.view_image_post.view.*
import kotlinx.android.synthetic.main.view_post.view.*
import kotlinx.android.synthetic.main.write_post_layout.view.*
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

class ProfileAdapter(
    private val onCreatePostButtonListener: OnCreatePostButtonListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var userResponse: UserResponse? = null
    var ownPosts: MutableList<Post> = mutableListOf()

    fun setDataSource(newOwnPosts: List<Post>): Disposable {
        return Single.fromCallable {
            DiffUtil.calculateDiff(BaseDiffCallback(ownPosts, newOwnPosts))
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                ownPosts = newOwnPosts.toMutableList()
                it.dispatchUpdatesTo(this)
            }, onError = {
                Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
            })
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_USER
            position == 1 -> TYPE_WRITE_NEW_POST
            ownPosts.elementAtOrNull(position - 2)?.photo == null -> TYPE_POST
            else -> TYPE_IMAGE_POST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_WRITE_NEW_POST -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.write_post_layout, parent, false)
                WritePostViewHolder(view, onCreatePostButtonListener)
            }
            TYPE_POST -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_view_post, parent, false)
                PostViewHolder(view)
            }
            TYPE_IMAGE_POST -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_view_image_post,
                        parent,
                        false
                    )
                ImagePostViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(
                    R.layout.profile_layout,
                    parent,
                    false
                )
                UserViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WritePostViewHolder -> holder.newPost
            is PostViewHolder -> holder.bind(ownPosts[position - 2])
            is ImagePostViewHolder -> holder.bind(ownPosts[position - 2])
            is UserViewHolder -> userResponse?.run(holder::bind)
        }
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bind(post: Post) {
            with(post) {
                Glide.with(itemView.context)
                    .load(groupAvatar)
                    .circleCrop()
                    .into(itemView.postViewGroup.avatarView)
                itemView.postViewGroup.name = groupName
                itemView.postViewGroup.dateView.text = dateTimeToString(localDateTime, itemView.context)
                itemView.postViewGroup.text = text
                if (isLiked) {
                    R.drawable.like
                } else {
                    R.drawable.unlike
                }.apply {
                    itemView.likeBtnView.setImageResource(this)
                }
            }
        }
    }

    class ImagePostViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        fun bind(post: Post) {
            with(post) {
                Glide.with(itemView.context)
                    .load(groupAvatar)
                    .circleCrop()
                    .into(itemView.postWithImageViewGroup.avatarImgView)
                itemView.postWithImageViewGroup.name = groupName
                itemView.postWithImageViewGroup.dateImgView.text = dateTimeToString(localDateTime, itemView.context)
                itemView.postWithImageViewGroup.text = text
                Glide.with(itemView.context)
                    .load(photo)
                    .into(itemView.postWithImageViewGroup.postImgView)
                if (isLiked) {
                    R.drawable.like
                } else {
                    R.drawable.unlike
                }.apply {
                    itemView.likeBtnImgView.setImageResource(this)
                }
            }
        }
    }

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(userResponse: UserResponse) {
            with(userResponse) {
                Glide.with(itemView.context)
                    .load(photo)
                    .circleCrop()
                    .into(itemView.photoProfileView)
                itemView.domain.text = domain
                itemView.nameProfileView.text = ("$firstName $lastName")
                val localDateTime = LocalDateTime(lastSeen?.time?.let { TimeUnit.SECONDS.toMillis(it) })
                itemView.lastSeenProfileView.text = dateTimeToString(localDateTime, itemView.context)
                itemView.bDateProfileView.text = if (bDate.isNullOrBlank()) {
                    itemView.context.getString(R.string.bdate, itemView.context.getString(R.string.emptyValue))
                } else {
                     itemView.context.getString(R.string.bdate, bDate)
                }
                itemView.cityProfileView.text = when {
                    city?.cityTitle.isNullOrBlank() -> {
                        itemView.context.getString(R.string.cityEmptyValue, country?.countryTitle)
                    }
                    country?.countryTitle.isNullOrBlank() -> {
                        itemView.context.getString(R.string.cityEmptyValue, city?.cityTitle)
                    }
                    city?.cityTitle.isNullOrBlank() && country?.countryTitle.isNullOrBlank() -> {
                        itemView.context.getString(R.string.cityEmptyValue, itemView.context.getString(R.string.emptyValue))
                    }
                    else -> itemView.context.getString(R.string.city, city?.cityTitle, country?.countryTitle)
                }
                itemView.educationProfileView.text =  if (universityName.isNullOrBlank() && facultyName.isNullOrBlank() && graduation == 0) {
                    itemView.context.getString(R.string.educationEmptyValue, itemView.context.getString(R.string.emptyValue))
                } else {
                     itemView.context.getString(R.string.education, universityName, facultyName, graduation)
                }
                itemView.careerProfileView.text = if (career?.firstOrNull()?.company.isNullOrBlank() && career?.firstOrNull()?.position.isNullOrBlank()) {
                    itemView.context.getString(R.string.careerEmpty, itemView.context.getString(R.string.emptyValue))
                } else {
                     itemView.context.getString(
                        R.string.career,
                        career?.firstOrNull()?.company,
                        career?.firstOrNull()?.from,
                        career?.firstOrNull()?.until,
                        career?.firstOrNull()?.position
                    )
                }
                itemView.followersCountProfileView.text = itemView.context.getString(R.string.followersCount, followersCount)
                if (about.isNullOrBlank()) {
                    itemView.aboutProfileView.text = itemView.context.getString(R.string.about, itemView.context.getString(R.string.emptyValue))
                } else {
                    itemView.aboutProfileView.text = itemView.context.getString(R.string.about, about)
                }
            }
        }
    }

    class WritePostViewHolder(view: View, onCreatePostButtonListener: OnCreatePostButtonListener) : RecyclerView.ViewHolder(view) {
        init {
            itemView.writePostBtn.setOnClickListener {
                onCreatePostButtonListener.onCreatePostButtonClick(newPost.text.toString())
                newPost.text.clear()
            }
        }
        val newPost: EditText = view.findViewById(R.id.writePost)
    }

    override fun getItemCount() = 2 + ownPosts.size

    private companion object {
        const val TYPE_POST = 1
        const val TYPE_IMAGE_POST = 2
        const val TYPE_USER = 3
        const val TYPE_WRITE_NEW_POST = 4
    }
}