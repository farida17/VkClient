package com.farida.coursework.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.*
import com.farida.coursework.R
import kotlinx.android.synthetic.main.view_post.view.*
import kotlin.math.max

class PostLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view_post, this, true)
    }

    var groupPhoto: Drawable? = null
        set(value) {
            avatarView.setImageDrawable(value)
        }
    var name: String
        set(value) {
            nameView.text = value
        }
        get() = nameView.text.toString()

    var date: String
        set(value) {
            dateView.text = value
        }
        get() = dateView.text.toString()

    var text: String
        set(value) {
            postTxtView.text = value
        }
        get() = postTxtView.text.toString()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = paddingTop + paddingBottom

        forEach { measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0) }

        height += max(
            avatarView.measuredHeight + avatarView.marginTop,
            nameView.measuredHeight + nameView.marginTop + dateView.measuredHeight + dateView.marginTop
        )
        height += postTxtView.measuredHeight + postTxtView.marginTop
        height += likeBtnView.measuredHeight + likeBtnView.marginTop + likeBtnView.marginBottom

        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        val currentRight = paddingRight
        var nameTop = paddingTop

        currentTop += avatarView.marginTop
        avatarView.layout(
            currentLeft + avatarView.marginStart,
            currentTop,
            currentLeft + avatarView.marginStart + avatarView.measuredWidth,
            currentTop + avatarView.measuredHeight
        )

        currentLeft += avatarView.marginStart + avatarView.measuredWidth + nameView.marginStart
        nameTop += nameView.marginTop
        nameView.layout(
            currentLeft,
            nameTop,
            currentLeft + nameView.measuredWidth,
            nameTop + nameView.measuredHeight
        )

        nameTop += nameView.measuredHeight
        dateView.layout(
            currentLeft,
            nameTop,
            currentLeft + dateView.measuredWidth,
            nameTop + dateView.measuredHeight
        )

        currentTop += avatarView.measuredHeight + postTxtView.marginTop
        postTxtView.layout(
            postTxtView.marginStart,
            currentTop,
            measuredWidth - postTxtView.marginEnd - currentRight,
            currentTop + postTxtView.measuredHeight
        )

        currentTop += postTxtView.measuredHeight + likeBtnView.marginTop
        currentLeft = likeBtnView.marginLeft
        likeBtnView.layout(
            currentLeft,
            currentTop,
            currentLeft + likeBtnView.measuredWidth,
            measuredHeight - likeBtnView.marginBottom
        )

        currentLeft += likeBtnView.measuredWidth + commentBtnView.marginStart
        commentBtnView.layout(
            currentLeft,
            currentTop,
            currentLeft + commentBtnView.measuredWidth,
            currentTop + commentBtnView.measuredHeight
        )

        currentLeft += commentBtnView.measuredWidth + shareBtnView.marginStart
        shareBtnView.layout(
            currentLeft,
            currentTop,
            currentLeft + shareBtnView.measuredWidth,
            currentTop + shareBtnView.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(
        LayoutParams.WRAP_CONTENT,
        LayoutParams.WRAP_CONTENT
    )
}