package com.farida.coursework.ui.touchHelper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.farida.coursework.R
import com.farida.coursework.ui.postsList.PostListAdapter
import com.farida.coursework.util.Utils.dpToPx
import com.farida.coursework.util.Utils.spToPx

class ItemTouchHelperCallback(var adapter: ItemTouchHelperAdapter, private val context: Context)
    : ItemTouchHelper.SimpleCallback(UP or DOWN, LEFT or RIGHT) {

    private val paint = Paint().apply {
        color = Color.WHITE
        textSize = spToPx(TEXT_SIZE, context)
    }
    private val textBounds = Rect()
    private var backgroundLike = ColorDrawable(Color.RED)
    private val backgroundHide = ColorDrawable(Color.BLUE)

    override fun onMove(recyclerView: RecyclerView, source: ViewHolder, target: ViewHolder): Boolean {
        if (source.itemViewType != target.itemViewType)
            return false
        adapter.onItemMove(source.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: ViewHolder, i: Int) {
        if (i == LEFT) {
            adapter.onHidePost(viewHolder.adapterPosition)
        } else if (i == RIGHT) {
            val position = viewHolder.adapterPosition
            adapter.onLikeButtonClick(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        if (dX > 0) {
            val post = (recyclerView.adapter as PostListAdapter).posts[viewHolder.adapterPosition]
            val likeText = if (post.isLiked) context.getString(R.string.dislikeText) else
                    context.getString(R.string.likeText)
            val itemView = viewHolder.itemView
            backgroundLike.setBounds(
                itemView.left,
                itemView.top,
                (itemView.left + dX).toInt(),
                itemView.bottom
            )
            backgroundLike.draw(c)
            paint.getTextBounds(likeText, 0, likeText.length, textBounds)
            val leftOffset = LIKE_TEXT_LEFT_OFFSET.dpToPx(context)
            c.drawText(
                likeText,
                leftOffset,
                itemView.top + (itemView.height + textBounds.bottom - textBounds.top) / TEXT_DRAW_PARAM,
                paint
            )
        } else if (dX < 0) {
            val itemView = viewHolder.itemView
            backgroundHide.setBounds(
                (itemView.right + dX).toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            backgroundHide.draw(c)

            val hideText = context.getString(R.string.hideText)
            paint.getTextBounds(hideText, 0, hideText.length, textBounds)
            val leftOffset = HIDE_TEXT_LEFT_OFFSET.dpToPx(context)
            c.drawText(hideText, leftOffset, itemView.top + (itemView.height + textBounds.bottom - textBounds.top) / TEXT_DRAW_PARAM, paint)
        }
    }

    private companion object {
        const val TEXT_SIZE = 80f
        const val LIKE_TEXT_LEFT_OFFSET = 50
        const val HIDE_TEXT_LEFT_OFFSET = 120
        const val TEXT_DRAW_PARAM = 2f
    }
}