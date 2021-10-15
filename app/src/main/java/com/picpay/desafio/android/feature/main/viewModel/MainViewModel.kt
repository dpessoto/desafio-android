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
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(private val repository: MainRepository) : BaseViewModel() {
    private val _listUser = MutableLiveData<Pair<ArrayList<User>, Boolean>>()
    val listUser: LiveData<Pair<ArrayList<User>, Boolean>>
        get() = _listUser

    private val _listSearchUser = MutableLiveData<ArrayList<User>>()
    val listSearchUser: LiveData<ArrayList<User>>
        get() = _listSearchUser

    private var notChangeList = ArrayList<User>()

    fun getUsers() {
        viewModelScope.launch {
            showLoading()
            repository.getUsers().collect {
                when (it) {
                    is ResultRepository.Success -> dataLoaded(it.data)
                    is ResultRepository.Error -> error(it)
                }
            }
        }
    }

    fun searchContacts(contact: String) {
        if ("" == contact) {
            _listSearchUser.value = notChangeList
        } else {
            _listSearchUser.value = ArrayList(notChangeList.filter { item ->
                val name = item.name.lowercase(Locale.getDefault())
                val username = item.username.lowercase(Locale.getDefault())
                name.contains(contact) || username.contains(contact)
            })
        }
    }

    private fun error(it: ResultRepository.Error) {
        _error.value = it.exception
        _showLoading.value = false
    }

    private fun dataLoaded(result: Pair<ArrayList<User>, Boolean>) {
        _listUser.value = result
        notChangeList = result.first
        _showLoading.value = false
    }

    private fun showLoading() {
        val value = _listUser.value
        if (value == null || value.first.isNullOrEmpty()) {
            _showLoading.value = true
        }
    }
}