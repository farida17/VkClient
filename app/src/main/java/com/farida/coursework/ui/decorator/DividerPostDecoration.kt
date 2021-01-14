package com.farida.coursework.ui.decorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.farida.coursework.R
import com.farida.coursework.util.Utils.dpToPx
import kotlinx.android.synthetic.main.date_divider.view.*
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

class DividerPostDecoration(private val context: Context) : RecyclerView.ItemDecoration() {

    private var dateDivider: View? = null
    private val dateFormat: DateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT)

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        val adapter = parent.adapter
        if (adapter is DecorationTypeProvider) {
            parent.children.forEach { child ->
                val childAdapterPosition = parent.getChildAdapterPosition(child)
                val type = adapter.getType(childAdapterPosition)
                if (type is DecorationType.WithDate) {
                    val dateView = getDateDivider(parent)
                    dateView.dividerDate.text = dateToString(type.date)
                    c.save()
                    c.translate(0f, (child.top - dateView.height).toFloat())
                    dateView.draw(c)
                    c.restore()
                }
            }
        } else {
            super.onDraw(c, parent, state)
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = OFFSET_BOTTOM.dpToPx(view.context).toInt()
        val position = parent.getChildAdapterPosition(view)
        if (position == RecyclerView.NO_POSITION) {
            return
        }
        val adapter = parent.adapter
        if (adapter is DecorationTypeProvider) {
            val type = adapter.getType(position)
            outRect.top = if (type is DecorationType.Space) {
                OFFSET_TOP.dpToPx(view.context).toInt()
            } else {
                getDateDivider(parent).height
            }
        }
    }

    private fun getDateDivider(parent: RecyclerView): View {
        if (dateDivider == null) {
            dateDivider = LayoutInflater.from(parent.context)
                .inflate(R.layout.date_divider, parent, false)

            dateDivider !! fixLayoutSizeIn parent
        }
        return dateDivider !!
    }

    private infix fun View.fixLayoutSizeIn(parent: ViewGroup) {
        if (layoutParams == null) {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val widthSpec =
            View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)

        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidth = ViewGroup.getChildMeasureSpec(
            widthSpec,
            parent.paddingLeft + parent.paddingRight,
            layoutParams.width
        )

        val childHeight = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            layoutParams.height
        )

        measure(childWidth, childHeight)
        layout(0, 0, measuredWidth, measuredHeight)
    }

    private fun dateToString(localDate: LocalDate): String {
        val now = LocalDate.now()
        return when {
            localDate.isEqual(now) -> context.getString(R.string.today)
            localDate.isEqual(now.minusDays(1)) -> context.getString(R.string.yesterday)
            else -> localDate.toString(dateFormat)
        }
    }

    private companion object {
        const val OFFSET_TOP = 8
        const val OFFSET_BOTTOM = 8
        const val DATE_FORMAT = "dd MMMM YYYY"
    }
}
