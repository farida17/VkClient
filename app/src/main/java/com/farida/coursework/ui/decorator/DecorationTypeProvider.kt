package com.farida.coursework.ui.decorator

interface DecorationTypeProvider {
    fun getType(position: Int): DecorationType
}
