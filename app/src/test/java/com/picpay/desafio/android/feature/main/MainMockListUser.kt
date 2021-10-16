package com.picpay.desafio.android.feature.main

import androidx.lifecycle.Observer
import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.model.User
import com.picpay.desafio.android.model.UserDAO
import com.picpay.desafio.android.model.UserDataBase
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

object MainMockListUser {
    val listUser =
        ArrayList<User>(
            listOf(
                User(1, "daniel", "dpessoto", "url"),
                User(2, "amanda", "asilva", "url")
            )
        )
}

@ExperimentalCoroutinesApi
object MainMockViewModel {
    val repository = mockk<MainRepository>()
    val dataLoadedObserver = mockk<Observer<Pair<List<User>, Boolean>>>(relaxed = true)
    val errorObserver = mockk<Observer<Exception>>(relaxed = true)
    val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
    val dispatcher = TestCoroutineDispatcher()
}

object MainMockRepository {
    val service = mockk<PicPayService>()
    val dataBase = mockk<UserDataBase>()
    val userDAO = mockk<UserDAO>()
}