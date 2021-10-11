package com.picpay.desafio.android.util.extension

import android.graphics.drawable.TransitionDrawable
import android.view.View
import androidx.core.content.ContextCompat

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setTransitionBackgroundDrawble(drawble: Int, durationMillis: Int) {
    val transitionDrawable = ContextCompat.getDrawable(this.context, drawble) as TransitionDrawable
    this.background = transitionDrawable
    transitionDrawable.startTransition(durationMillis)
}