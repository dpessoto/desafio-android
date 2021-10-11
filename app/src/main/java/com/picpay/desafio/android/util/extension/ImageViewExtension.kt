package com.picpay.desafio.android.util.extension

import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

fun ImageView.loadImage(image: String, errorDrawable: Int, onSuccess: () -> Unit, onError: () -> Unit) {
    Picasso.get()
        .load(image)
        .error(errorDrawable)
        .into(this, object : Callback {
            override fun onSuccess() {
                onSuccess()
            }

            override fun onError(e: Exception?) {
                onError()
            }
        })
}
