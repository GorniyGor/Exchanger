package com.recan.exchanger.di

import com.recan.exchanger.BuildConfig
import com.recan.exchanger.data.ExchangerApi
import com.recan.exchanger.data.repository.ExchangerRepository
import com.recan.exchanger.domain.repository.IExchangerRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun getRepository(api: ExchangerApi): IExchangerRepository = ExchangerRepository(api)

    @Singleton
    @Provides
    fun getApi(): ExchangerApi = Retrofit.Builder()
        .baseUrl(BuildConfig.EXCHANGER_API)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(ExchangerApi::class.java)
}