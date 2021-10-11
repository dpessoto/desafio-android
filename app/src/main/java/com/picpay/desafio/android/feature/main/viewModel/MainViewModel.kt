package com.picpay.desafio.android.feature.main.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.StateView
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MainRepository) : ViewModel() {
    private val _stateView = MutableLiveData<StateView<Pair<List<User>, Boolean>>>()
    val stateView: LiveData<StateView<Pair<List<User>, Boolean>>>
        get() = _stateView

    fun getUser() {
        viewModelScope.launch {
            _stateView.value = StateView.Loading

            when (val result = repository.getUser()) {
                is ResultRepository.Success -> {
                    _stateView.value = StateView.DataLoaded(result.data)
                }
                is ResultRepository.Error -> {
                    _stateView.value = StateView.Error(result.exception)
                }
            }
        }
    }
}