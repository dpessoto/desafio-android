package com.picpay.desafio.android.feature.base.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    protected val _showLoading = MutableLiveData<Boolean>()
    val showLoading: LiveData<Boolean>
    get() = _showLoading

    protected val _error = MutableLiveData<Exception>()
    val error: LiveData<Exception>
        get() = _error
}