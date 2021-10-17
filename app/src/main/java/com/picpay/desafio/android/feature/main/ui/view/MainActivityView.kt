package com.picpay.desafio.android.feature.main.ui.view

import com.picpay.desafio.android.model.User

interface MainActivityView {
    fun setVisibilitySwipeAndError()
    fun stateError(e: Throwable)
    fun stateDataLoaded(list: ArrayList<User>, remote: Boolean)
    fun stateSearchLoaded(list: ArrayList<User>)
    fun setRecyclerView()
    fun setEvents()
    fun setObservers()
    fun removeObservers()
}