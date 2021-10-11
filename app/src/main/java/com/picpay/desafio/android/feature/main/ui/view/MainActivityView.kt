package com.picpay.desafio.android.feature.main.ui.view

import com.picpay.desafio.android.model.User

interface MainActivityView {
    fun setVisibilitySwipeAndError(swipeVisibility: Int, errorVisibility: Int)
    fun stateError(e: Throwable)
    fun stateDataLoaded(list: List<User>, remote: Boolean)
    fun setRecyclerView()
    fun setEvents()
    fun setObservers()
    fun removeObservers()
}