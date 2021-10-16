package com.picpay.desafio.android.util.extension

import android.app.Activity
import android.graphics.drawable.TransitionDrawable
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.setTransitionBackgroundDrawable(drawble: Int, durationMillis: Int) {
    val transitionDrawable = ContextCompat.getDrawable(context, drawble) as TransitionDrawable
    this.background = transitionDrawable
    transitionDrawable.startTransition(durationMillis)
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun View.setBackgroundDrawable(value: Int) {
    val sdk = android.os.Build.VERSION.SDK_INT;
    val drawable = ContextCompat.getDrawable(context, value)
    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        setBackgroundDrawable(drawable);
    } else {
        background = drawable;
    }
}