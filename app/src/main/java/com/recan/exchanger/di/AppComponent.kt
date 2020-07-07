package com.recan.exchanger.di

import com.recan.exchanger.App
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Scope
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidInjectionModule::class,
    ActivityBindingModule::class,
    RepositoryModule::class
])
interface AppComponent : AndroidInjector<App>


@Scope
annotation class FlowScope