package com.picpay.desafio.android.util.extension

fun <T> List<T>.toArrayList() : ArrayList<T> {
    return ArrayList(this)
}