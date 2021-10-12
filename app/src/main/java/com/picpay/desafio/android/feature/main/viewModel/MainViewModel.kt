package com.picpay.desafio.android.feature.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.feature.base.viewModel.BaseViewModel
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {
    private val _listUser = MutableLiveData<Pair<List<User>, Boolean>>()
    val listUser: LiveData<Pair<List<User>, Boolean>>
        get() = _listUser

    fun getUser() {
        viewModelScope.launch {
            showLoading()
            repository.getUser().collect {
                when (it) {
                    is ResultRepository.Success -> dataLoaded(it.data)
                    is ResultRepository.Error -> error(it)
                }
            }
        }
    }

    private fun error(it: ResultRepository.Error) {
        _error.value = it.exception
        _showLoading.value = false
    }

    private fun dataLoaded(result: Pair<List<User>, Boolean>) {
        _listUser.value = result
        _showLoading.value = false
    }

    private fun showLoading() {
        val value = _listUser.value
        if (value == null || value.first.isNullOrEmpty()) {
            _showLoading.value = true
        }
    }
}