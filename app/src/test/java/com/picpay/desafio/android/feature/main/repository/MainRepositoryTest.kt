package com.picpay.desafio.android.feature.main.repository

import com.picpay.desafio.android.feature.main.MainMockListUser.listUser
import com.picpay.desafio.android.feature.main.MainMockRepository.dataBase
import com.picpay.desafio.android.feature.main.MainMockRepository.service
import com.picpay.desafio.android.feature.main.MainMockRepository.userDAO
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MainRepositoryTest {

    private val repository = initRepository()

    @Test
    fun remoteGetUsers() {
        runBlocking {
            coEvery { service.getUsers() } returns listUser

            val users = service.getUsers()
            repository.getUsers()
            assertEquals(users, listUser)
        }
    }

    @Test
    fun dataGetUsers() {
        runBlocking {
            coEvery { userDAO.getAllUsers() } returns listUser

            val users = userDAO.getAllUsers()
            repository.getUsers()
            assertEquals(users, listUser)
        }
    }

    private fun initRepository(): MainRepository = MainRepositoryImpl(service, dataBase, userDAO)

}