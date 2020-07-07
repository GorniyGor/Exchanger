package com.recan.exchanger.di.feature_module

import com.recan.exchanger.di.FlowScope
import com.recan.exchanger.presentation.base.PresenterFactory
import com.recan.exchanger.presentation.exchaner.ExchangerPresenter
import dagger.Module
import dagger.Provides

@Module
class ExchangerModule {

    @FlowScope
    @Provides
    fun getPresenterFactory(presenter: ExchangerPresenter) =
        object : PresenterFactory<ExchangerPresenter.ExchangerView, ExchangerPresenter> {
            override fun get(): ExchangerPresenter = presenter
        }
}