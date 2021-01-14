package com.farida.coursework.util

import android.content.Context
import android.util.TypedValue
import com.farida.coursework.R
import org.joda.time.LocalDate
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

object Utils {

    const val BASE_URL_VALUE = "https://api.vk.com/method/"

    fun Int.dpToPx(context: Context) = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        context.resources.displayMetrics
    )

    fun spToPx(sp: Float, context: Context): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
    }

    private val dateTimeFormat: DateTimeFormatter = DateTimeFormat.forPattern("dd MMMM в kk:mm")

    fun dateTimeToString(localDateTime: LocalDateTime, context: Context): String {
        val today = LocalDate.now()
        return when {
            localDateTime.toLocalDate().isEqual(today)
            -> context.getString(R.string.today_at, localDateTime.toString("kk:mm"))
            localDateTime.toLocalDate().isEqual(today.minusDays(1))
            -> context.getString(R.string.yesterday_at, localDateTime.toString("kk:mm"))
            else -> localDateTime.toString(dateTimeFormat)
        }
    }
}