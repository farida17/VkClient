package com.farida.coursework.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.core.view.*
import com.farida.coursework.R
import kotlinx.android.synthetic.main.view_image_post.view.*
import kotlinx.android.synthetic.main.view_post.view.*
import kotlin.math.max

class PostWithImageLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr) {

    var groupPhoto: Drawable? = null
        set(value) {
            avatarView.setImageDrawable(value)
        }
    var name: String
        set(value) {
            nameImgView.text = value
        }
        get() = nameImgView.text.toString()

    var date: String
        set(value) {
            dateImgView.text = value
        }
        get() = dateImgView.text.toString()

    var text: String
        set(value) {
            postTxtImgView.text = value
        }
        get() = postTxtView.text.toString()

    var postImg: Drawable? = null
        set(value) {
            postImgView.setImageDrawable(value)
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_image_post, this, true)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = MeasureSpec.getSize(widthMeasureSpec)
        var height = paddingTop + paddingBottom

        forEach { measureChildWithMargins(it, widthMeasureSpec, 0, heightMeasureSpec, 0) }

        height += max(avatarImgView.measuredHeight + avatarImgView.marginTop,
            nameImgView.measuredHeight + nameImgView.marginTop + dateImgView.measuredHeight + dateImgView.marginTop
        )
        height += postTxtImgView.measuredHeight + postTxtImgView.marginTop
        height += postImgView.measuredHeight + postImgView.marginTop
        height += likeBtnImgView.measuredHeight + likeBtnImgView.marginTop + likeBtnImgView.marginBottom

        setMeasuredDimension(desiredWidth, resolveSize(height, heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var currentLeft = paddingLeft
        var currentTop = paddingTop
        val currentRight = paddingRight
        var nameTop = paddingTop

        currentTop += avatarImgView.marginTop
        avatarImgView.layout(
            currentLeft + avatarImgView.marginStart,
            currentTop,
            currentLeft + avatarImgView.marginStart + avatarImgView.measuredWidth,
            currentTop + avatarImgView.measuredHeight
        )

        currentLeft += avatarImgView.marginStart + avatarImgView.measuredWidth + nameImgView.marginStart
        nameTop += nameImgView.marginTop
        nameImgView.layout(
            currentLeft,
            nameTop,
            currentLeft + nameImgView.measuredWidth,
            nameTop + nameImgView.measuredHeight
        )

        nameTop += nameImgView.measuredHeight
        dateImgView.layout(
            currentLeft,
            nameTop,
            currentLeft + dateImgView.measuredWidth,
            nameTop + dateImgView.measuredHeight
        )

        currentTop += avatarImgView.measuredHeight + postTxtImgView.marginTop
        currentLeft = paddingLeft
        postTxtImgView.layout(
            currentLeft + postTxtImgView.marginStart,
            currentTop,
            measuredWidth - postTxtImgView.marginEnd - currentRight,
            currentTop + postTxtImgView.measuredHeight
        )

        currentTop += postTxtImgView.measuredHeight + postImgView.marginTop
        postImgView.layout(
            currentLeft + postImgView.marginStart,
            currentTop,
            measuredWidth - postImgView.marginEnd - currentRight,
            currentTop + postImgView.measuredHeight
        )

        currentTop += postImgView.measuredHeight + likeBtnImgView.marginTop
        currentLeft = paddingLeft + likeBtnImgView.marginLeft
        likeBtnImgView.layout(
            currentLeft,
            currentTop,
            currentLeft + likeBtnImgView.measuredWidth,
            measuredHeight - likeBtnImgView.marginBottom
        )

        currentLeft += likeBtnImgView.measuredWidth + commentBtnImgView.marginStart
        commentBtnImgView.layout(
            currentLeft,
            currentTop,
            currentLeft + commentBtnImgView.measuredWidth,
            currentTop + commentBtnImgView.measuredHeight
        )

        currentLeft += commentBtnImgView.measuredWidth + shareBtnImgView.marginStart
        shareBtnImgView.layout(
            currentLeft,
            currentTop,
            currentLeft + shareBtnImgView.measuredWidth,
            currentTop + shareBtnImgView.measuredHeight
        )

        currentLeft += shareBtnImgView.measuredWidth + saveImgBtnImgView.marginStart
        saveImgBtnImgView.layout(
            currentLeft,
            currentTop,
            currentLeft + saveImgBtnImgView.measuredWidth,
            currentTop + saveImgBtnImgView.measuredHeight
        )
    }

    override fun generateLayoutParams(attrs: AttributeSet?) = MarginLayoutParams(context, attrs)

    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
}