package com.farida.coursework.ui.decorator

import org.joda.time.LocalDate

sealed class DecorationType {
    object Space : DecorationType()

    class WithDate(
        val date: LocalDate
    ) : DecorationType()
}
