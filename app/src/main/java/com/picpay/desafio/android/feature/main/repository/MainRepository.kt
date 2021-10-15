package com.picpay.desafio.android.feature.main.repository

import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    suspend fun getUsers(): Flow<ResultRepository<Pair<List<User>, Boolean>>>
}