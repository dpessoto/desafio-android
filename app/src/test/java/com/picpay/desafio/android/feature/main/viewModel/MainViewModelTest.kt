package com.picpay.desafio.android.feature.main.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.model.ResultRepository
import com.picpay.desafio.android.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val repository = mockk<MainRepository>()
    private val dataLoadedObserver = mockk<Observer<Pair<List<User>, Boolean>>>(relaxed = true)
    private val errorObserver = mockk<Observer<Exception>>(relaxed = true)
    private val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)

    private val dispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun success() {
        val viewModel = initViewModel()
        val listUser =
            listOf(User(1, "daniel", "pessoto", "url"), User(1, "amanda", "pessoto", "url"))
        val success = ResultRepository.Success(Pair(listUser, true))
        val flow = flow {
            emit(success)
        }
        coEvery { repository.getUser() } returns flow

        viewModel.getUser()

        coVerify { repository.getUser() }
        verify { dataLoadedObserver.onChanged(success.data) }
    }

    @Test
    fun error() {
        val viewModel = initViewModel()
        val error = ResultRepository.Error(Exception())
        val flow = flow {
            emit(error)
        }
        coEvery { repository.getUser() } returns flow

        viewModel.getUser()

        coVerify { repository.getUser() }
        verify { errorObserver.onChanged(error.exception) }
    }

    @Test
    fun loading() {
        val viewModel = initViewModel()
        val error = ResultRepository.Error(Exception())
        val flow = flow {
            emit(error)
        }
        coEvery { repository.getUser() } returns flow

        viewModel.getUser()

        coVerify { repository.getUser() }
        verify { loadingObserver.onChanged(true) }
    }

    private fun initViewModel(): MainViewModel {
        val viewModel = MainViewModel(repository)

        viewModel.listUser.observeForever(dataLoadedObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.showLoading.observeForever(loadingObserver)

        return viewModel
    }

}