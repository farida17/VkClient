package com.farida.coursework.ui.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farida.coursework.R
import com.farida.coursework.model.Comment
import com.farida.coursework.model.Post
import com.farida.coursework.util.Utils.dateTimeToString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.list_view_comment.view.*
import kotlinx.android.synthetic.main.list_view_image_post.view.*
import kotlinx.android.synthetic.main.view_comment.view.*
import kotlinx.android.synthetic.main.view_image_post.view.*
import kotlinx.android.synthetic.main.write_comment_layout.view.*
import org.joda.time.LocalDateTime
import java.util.concurrent.TimeUnit

class PostAdapter(
    private val onSaveImageListener: OnSaveImageListener,
    private val onShareImageListener: OnShareImageListener,
    private val onCreateCommentButtonListener: OnCreateCommentButtonListener,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var post: Post? = null
    var comments: MutableList<Comment> = mutableListOf()

    fun setDataSource(newComments: List<Comment>): Disposable {
        return Single.fromCallable {
            DiffUtil.calculateDiff(CommentDiffCallback(comments, newComments))
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onSuccess = {
                comments = newComments.toMutableList()
                it.dispatchUpdatesTo(this)
            }, onError = {
                Toast.makeText(context, R.string.error_message, Toast.LENGTH_SHORT).show()
            })
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TYPE_IMAGE_POST
            1 -> TYPE_WRITE_NEW_COMMENT
            else -> TYPE_COMMENT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_WRITE_NEW_COMMENT -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.write_comment_layout, parent, false)
                WritePostViewHolder(view, onCreateCommentButtonListener)
            }
            TYPE_COMMENT -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_view_comment, parent, false)
                CommentsViewHolder(view)
            }
            else -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.list_view_image_post,
                        parent,
                        false
                    )
                ImagePostViewHolder(view, onShareImageListener, onSaveImageListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is WritePostViewHolder -> holder.newComment
            is ImagePostViewHolder -> post?.run(holder::bind)
            is CommentsViewHolder -> holder.bind(comments[position - 2]) }
    }

    override fun getItemCount() = comments.size + 2

    class ImagePostViewHolder(view: View, private val shareListener: OnShareImageListener, private val saveImageListener: OnSaveImageListener) : RecyclerView.ViewHolder(view) {

        fun bind(post: Post) {
            with(post) {
                itemView.imageCardView
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
                if (!photo.isNullOrBlank()) {
                    itemView.saveImgBtnImgView.setOnClickListener {
                        saveImageListener.saveImage(itemView.postImgView.drawToBitmap())
                    }
                    itemView.shareBtnImgView.setOnClickListener {
                        shareListener.shareImage(itemView.postImgView.drawToBitmap())
                    }
                }
            }
        }
    }

    class CommentsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(comment: Comment) {
            with(comment) {
                if (isDeleted) {
                    itemView.commentView.text = itemView.context.getString(R.string.deletedComment)
                } else {
                    Glide.with(itemView.context)
                        .load(groupAvatar)
                        .circleCrop()
                        .into(itemView.commentView.avatarCommentView)
                    itemView.commentView.name = groupName
                    val localDateTime = LocalDateTime(TimeUnit.SECONDS.toMillis(date))
                    itemView.commentView.date = dateTimeToString(localDateTime, itemView.context)
                    itemView.commentView.text = text
                    Glide.with(itemView.context)
                        .load(attachments?.firstOrNull()?.photo?.sizes?.lastOrNull()?.url)
                        .into(itemView.commentView.imgCommentView)
                }
            }
        }
    }

    class WritePostViewHolder(view: View, onCreateCommentButtonListener: OnCreateCommentButtonListener) : RecyclerView.ViewHolder(view) {
        init {
            itemView.writeCommentBtn.setOnClickListener {
                onCreateCommentButtonListener.onCreateCommentButtonClick(newComment.text.toString())
                newComment.text.clear()
            }
        }
        val newComment: EditText = view.findViewById(R.id.writeComment)
    }

    private companion object {
        const val TYPE_COMMENT = 1
        const val TYPE_IMAGE_POST = 2
        const val TYPE_WRITE_NEW_COMMENT = 3
    }
}