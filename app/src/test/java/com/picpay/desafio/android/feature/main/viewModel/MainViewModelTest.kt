package com.picpay.desafio.android.feature.main.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.feature.main.MainMockListUser.listUser
import com.picpay.desafio.android.feature.main.MainMockViewModel.dataLoadedObserver
import com.picpay.desafio.android.feature.main.MainMockViewModel.dispatcher
import com.picpay.desafio.android.feature.main.MainMockViewModel.errorObserver
import com.picpay.desafio.android.feature.main.MainMockViewModel.loadingObserver
import com.picpay.desafio.android.feature.main.MainMockViewModel.repository
import com.picpay.desafio.android.model.ResultRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
        val success = ResultRepository.Success(Pair(listUser, true))
        val flow = flow {
            emit(success)
        }
        coEvery { repository.getUsers() } returns flow

        viewModel.getUsers()

        coVerify { repository.getUsers() }
        verify { dataLoadedObserver.onChanged(success.data) }
    }

    @Test
    fun error() {
        val viewModel = initViewModel()
        val error = ResultRepository.Error(Exception())
        val flow = flow {
            emit(error)
        }
        coEvery { repository.getUsers() } returns flow

        viewModel.getUsers()

        coVerify { repository.getUsers() }
        verify { errorObserver.onChanged(error.exception) }
    }

    @Test
    fun loading() {
        val viewModel = initViewModel()
        val error = ResultRepository.Error(Exception())
        val flow = flow {
            emit(error)
        }
        coEvery { repository.getUsers() } returns flow

        viewModel.getUsers()

        coVerify { repository.getUsers() }
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