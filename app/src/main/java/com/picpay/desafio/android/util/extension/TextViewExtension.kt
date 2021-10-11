package com.picpay.desafio.android.util.extension

import android.graphics.Paint
import android.widget.TextView

fun TextView.toUnderline() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.setTextToUnderline(text: String) {
    this.text = text
    this.toUnderline()
}
