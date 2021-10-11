package com.picpay.desafio.android.feature.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.feature.base.viewModel.BaseViewModel
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.StateView
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {
    private val _stateView = MutableLiveData<StateView<Pair<List<User>, Boolean>>>()
    val stateView: LiveData<StateView<Pair<List<User>, Boolean>>>
        get() = _stateView

    private var listUser = listOf<User>()

    fun getUser() {
        viewModelScope.launch {
            if (listUser.isNullOrEmpty())
                _showLoading.value = true

            when (val result = repository.getUser()) {
                is ResultRepository.Success -> {
                    listUser = result.data.first
                    _stateView.value = StateView.DataLoaded(result.data)
                    _showLoading.value = false
                }
                is ResultRepository.Error -> {
                    _stateView.value = StateView.Error(result.exception)
                    _showLoading.value = false
                }
            }
        }
    }
}