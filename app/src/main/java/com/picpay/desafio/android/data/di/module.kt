package com.picpay.desafio.android.data.di

import com.picpay.desafio.android.data.network.PicPayService
import com.picpay.desafio.android.feature.main.repository.MainRepositoryImpl
import com.picpay.desafio.android.feature.main.ui.UserListAdapter
import com.picpay.desafio.android.feature.main.viewModel.MainViewModel
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
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    factory {
        MainRepositoryImpl(
            service = get()
        )
    }

    viewModel {
        MainViewModel(repository = get())
    }

}

val uiModule = module {
    factory { UserListAdapter() }
}