package com.picpay.desafio.android.data.di

import androidx.room.Room
import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.feature.main.repository.MainRepository
import com.picpay.desafio.android.feature.main.repository.MainRepositoryImpl
import com.picpay.desafio.android.feature.main.ui.adapter.UserListAdapter
import com.picpay.desafio.android.feature.main.viewModel.MainViewModel
import com.picpay.desafio.android.model.UserDataBase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val dataModule = module {
    single {
        Retrofit.Builder()
            .client(get())
            .baseUrl("https://609a908e0f5a13001721b74e.mockapi.io/picpay/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PicPayService::class.java)
    }

    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build()
    }

    single {
        Room.databaseBuilder(get(), UserDataBase::class.java, "picpay_database").build()
    }

    factory {
        MainRepositoryImpl(service = get(), dataBase = get()) as MainRepository
    }

    viewModel {
        MainViewModel(repository = get())
    }

}

val uiModule = module {
    factory { UserListAdapter() }
}