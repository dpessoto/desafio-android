package com.picpay.desafio.android.feature.main.repository

import androidx.room.withTransaction
import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.model.UserDataBase

class MainRepositoryImpl(private val service: PicPayService, private val dataBase: UserDataBase) :
    MainRepository {
    private val userDAO = dataBase.userDAO()

    override suspend fun getUser(): ResultRepository<Pair<List<User>, Boolean>> {
        return try {
            val users = service.getUsers()
            dataBase.withTransaction {
                userDAO.deleteAllUsers()
                userDAO.insertUsers(users)
            }

            ResultRepository.Success(Pair(users, true))

        } catch (e: Exception) {
            val users = userDAO.getAllUsers()
            if (!users.isNullOrEmpty())
                ResultRepository.Success(Pair(users, false))
            else
                ResultRepository.Error(e)
        }
    }
}