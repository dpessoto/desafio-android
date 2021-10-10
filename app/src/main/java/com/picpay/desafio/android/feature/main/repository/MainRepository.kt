package com.picpay.desafio.android.feature.main.repository

import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User

interface MainRepository {
    suspend fun getUser(): ResultRepository<Pair<List<User>, Boolean>>
}