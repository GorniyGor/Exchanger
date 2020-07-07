package com.recan.exchanger.di

import com.recan.exchanger.di.feature_module.ExchangerModule
import com.recan.exchanger.presentation.exchaner.ExchangerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {

    @FlowScope
    @ContributesAndroidInjector(modules = [ExchangerModule::class])
    internal abstract fun exchangeActivity(): ExchangerActivity
}