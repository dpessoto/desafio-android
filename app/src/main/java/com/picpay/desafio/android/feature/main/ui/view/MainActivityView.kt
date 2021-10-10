package com.picpay.desafio.android.feature.main.ui.view

import com.picpay.desafio.android.model.User

interface MainActivityView {
    fun setVisibilityScrollAndError(scroll: Int, error: Int)
    fun showSnackBarMessage(message: String)
    fun stateError(e: Throwable)
    fun stateDataLoaded(list: List<User>, remote: Boolean)
    fun setRecylerView()
    fun setClicks()
}