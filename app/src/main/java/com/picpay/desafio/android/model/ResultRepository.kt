package com.picpay.desafio.android.model

sealed class ResultRepository<out T> {
    data class Success<out T>(val data: T) : ResultRepository<T>()
    data class Error(val exception: Exception) : ResultRepository<Nothing>()
}