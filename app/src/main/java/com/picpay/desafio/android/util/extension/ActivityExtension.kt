package com.picpay.desafio.android.util.extension

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup

fun Activity.addView(view: View) {
    try {
        (window.decorView as ViewGroup).addView(view)
    } catch (e: Exception) {
        Log.e("addView", e.message + "")
    }
}

fun Activity.removeView(view: View) {
    try {
        (window.decorView as ViewGroup).removeView(view)
    } catch (e: Exception) {
        Log.e("removeView", e.message + "")
    }
}