package com.recan.exchanger.presentation.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V : PresentationView> {
    protected val disposables = CompositeDisposable()
    protected var view: V? = null

    fun bingView(view: V) { this.view = view }

    fun unbindView() {
        disposables.clear()
        view = null
    }

    protected fun Disposable.clearAtTime() = disposables.add(this)
}