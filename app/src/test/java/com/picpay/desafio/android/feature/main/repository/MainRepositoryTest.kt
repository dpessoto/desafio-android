package com.picpay.desafio.android.feature.main.repository

import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.model.UserDAO
import com.picpay.desafio.android.model.UserDataBase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MainRepositoryTest {

    private val service = mockk<PicPayService>()
    private val dataBase = mockk<UserDataBase>()
    private val userDAO = mockk<UserDAO>()

    @Test
     fun remoteGetUsers() {
        runBlocking {
                    val repository = initRepository()
            val listUser = listOf(User(1, "daniel", "pessoto", "url"), User(1, "amanda", "pessoto", "url"))

            coEvery { service.getUsers() } returns listUser

            val users = service.getUsers()
            repository.getUsers()
            assertEquals(users, listUser)
        }
    }

    @Test
    fun dataGetUsers() {
        runBlocking {
            val repository = initRepository()
            val listUser = listOf(User(1, "daniel", "pessoto", "url"), User(1, "amanda", "pessoto", "url"))

            coEvery { userDAO.getAllUsers() } returns listUser

            val users = userDAO.getAllUsers()
            repository.getUsers()
            assertEquals(users, listUser)
        }
    }

    private fun initRepository(): MainRepository {
        val mainRepository = MainRepositoryImpl(service, dataBase, userDAO)

        return mainRepository
    }

}