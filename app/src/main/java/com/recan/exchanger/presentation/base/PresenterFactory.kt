package com.recan.exchanger.presentation.base

interface PresenterFactory<V : PresentationView, P : BasePresenter<V>> {
    fun get(): P
}
