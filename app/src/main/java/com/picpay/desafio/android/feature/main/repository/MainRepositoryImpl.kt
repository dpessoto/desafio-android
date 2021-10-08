package com.picpay.desafio.android.feature.main.repository

import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User

class MainRepositoryImpl(private val service: PicPayService): MainRepository {
    override suspend fun getUser(): ResultRepository<ArrayList<User>>{
        return try {
            ResultRepository.Success(service.getUsers())
        } catch (e: Exception) {
            ResultRepository.Error(e)
        }
    }
}