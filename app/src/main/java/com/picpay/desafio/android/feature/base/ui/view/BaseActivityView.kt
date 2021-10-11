package com.picpay.desafio.android.feature.base.ui.view

import android.view.Gravity
import android.view.View

interface BaseActivityView {
    fun showLoading()
    fun stopLoading()
    fun showSnackBarMessage(viewContext: View, message: String, postion: Int = Gravity.TOP)
}