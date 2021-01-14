package com.farida.coursework.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.*
import com.farida.coursework.R
import kotlinx.android.synthetic.main.view_comment.view.*
import kotlinx.android.synthetic.main.view_image_post.view.*
import kotlinx.android.synthetic.main.view_post.view.*

class CommentLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_comment, this, true)
    }

    var profilePhoto: Drawable? = null
        set(value) {
            avatarCommentView.setImageDrawable(value)
        }
    var name: String?
        set(value) {
            nameCommentView.text = value
        }
        get() = nameCommentView.text.toString()

    var date: String
        set(value) {
            dateCommentView.text = value
        }
        get() = dateView.text.toString()

    var text: String?
        set(value) {
            txtCommentView.text = value
        }
        get() = txtCommentView.text.toString()

    var commentImg: Drawable? = null
        set(value) {
            postImgView.setImageDrawable(value)
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = paddingTop + paddingBottom

        forEach { measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0) }

        height += nameCommentView.measuredHeight + nameCommentView.marginTop
        height += txtCommentView.measuredHeight + txtCommentView.marginTop
        height += imgCommentView.measuredHeight + imgCommentView.marginTop
        height += dateCommentView.measuredHeight + dateCommentView.marginTop + dateCommentView.marginBottom

        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        val currentRight = paddingRight

        avatarCommentView.layout(
            currentLeft + avatarCommentView.marginStart,
            currentTop + avatarCommentView.marginTop,
            currentLeft + avatarCommentView.marginStart + avatarCommentView.measuredWidth,
            currentTop + avatarCommentView.measuredHeight
        )

        currentLeft += avatarCommentView.marginStart + avatarCommentView.measuredWidth + nameCommentView.marginStart
        currentTop += nameCommentView.marginTop
        nameCommentView.layout(
            currentLeft,
            currentTop,
            currentLeft + nameCommentView.measuredWidth,
            currentTop + nameCommentView.measuredHeight
        )

        currentTop += nameCommentView.measuredHeight + txtCommentView.marginTop
        txtCommentView.layout(
            currentLeft,
            currentTop,
            measuredWidth - txtCommentView.marginEnd - currentRight,
            currentTop + txtCommentView.measuredHeight
        )

        currentTop += txtCommentView.measuredHeight + imgCommentView.marginTop
        imgCommentView.layout(
            currentLeft,
            currentTop,
            measuredWidth - imgCommentView.marginEnd - currentRight,
            currentTop + imgCommentView.measuredHeight
        )

        currentTop += imgCommentView.measuredHeight + dateCommentView.marginTop
        dateCommentView.layout(
            currentLeft,
            currentTop,
            currentLeft + dateCommentView.measuredWidth,
            measuredHeight - dateCommentView.marginBottom
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    )
}