package com.picpay.desafio.android.feature.main.repository

import androidx.room.withTransaction
import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.model.UserDAO
import com.picpay.desafio.android.model.UserDataBase
import com.picpay.desafio.android.util.extension.toArrayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class MainRepositoryImpl(private val service: PicPayService, private val dataBase: UserDataBase, private val userDAO: UserDAO) : MainRepository {
    constructor(service: PicPayService, dataBase: UserDataBase)
            : this(service, dataBase, dataBase.userDAO())

    override suspend fun getUsers(): Flow<ResultRepository<Pair<ArrayList<User>, Boolean>>> {
        return flow {
            try {
                val users = service.getUsers()
                dataBase.withTransaction {
                    userDAO.deleteAllUsers()
                    userDAO.insertUsers(users)
                }

                emit(ResultRepository.Success(Pair(users, true)))
            } catch (e: Exception) {
                try {
                    val users = userDAO.getAllUsers().toArrayList()
                    if (!users.isNullOrEmpty())
                        emit(ResultRepository.Success(Pair(users, false)))
                    else
                        emit(ResultRepository.Error(e))
                } catch (e: Exception) {
                    emit(ResultRepository.Error(e))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}