package com.farida.coursework.ui.likePostsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farida.coursework.R
import com.farida.coursework.model.Post
import com.farida.coursework.ui.base.BaseDiffCallback
import com.farida.coursework.ui.decorator.DecorationType
import com.farida.coursework.ui.decorator.DecorationTypeProvider
import com.farida.coursework.util.Utils.dateTimeToString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_view_image_post.view.*
import kotlinx.android.synthetic.main.list_view_post.view.*
import kotlinx.android.synthetic.main.view_image_post.view.*
import kotlinx.android.synthetic.main.view_post.view.*

class LikePostsListAdapter(val context: Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(), DecorationTypeProvider {

    var likePosts: MutableList<Post> = mutableListOf()

    fun setDataSource(newLikePosts: List<Post>): Disposable {
        return Single.fromCallable {
            DiffUtil.calculateDiff(BaseDiffCallback(likePosts, newLikePosts))
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                likePosts = newLikePosts.toMutableList()
                it.dispatchUpdatesTo(this)
            }, onError = {
                Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
            })
    }

    override fun getItemViewType(position: Int): Int {
        val post = likePosts[position]
        return if (post.photo == null) {
            TYPE_POST
        } else {
            TYPE_IMAGE_POST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_POST) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_view_post, parent, false)
            PostViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(parent.context).inflate(
                    R.layout.list_view_image_post,
                    parent,
                    false
                )
            ImagePostViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PostViewHolder -> holder.bind(likePosts[position])
            is ImagePostViewHolder -> holder.bind(likePosts[position])
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
                itemView.postTxtImgView.text = text
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

    override fun getItemCount() = likePosts.size

    override fun getType(position: Int): DecorationType {
        if (position == RecyclerView.NO_POSITION || likePosts.isEmpty()) {
            return DecorationType.Space
        }
        if (position == 0) {
            return DecorationType.WithDate(likePosts[0].localDate)
        }
        val current = likePosts[position]
        val previous = likePosts[position - 1]
        return if (current.localDate.dayOfYear().get() == previous.localDate.dayOfYear().get()) {
            DecorationType.Space
        } else {
            DecorationType.WithDate(likePosts[position].localDate)
        }
    }

    private companion object {
        const val TYPE_POST = 1
        const val TYPE_IMAGE_POST = 2
    }
}