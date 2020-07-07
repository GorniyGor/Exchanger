package com.recan.exchanger.presentation.base

import android.os.Bundle
import dagger.android.DaggerActivity
import javax.inject.Inject

open class BaseActivity<V : PresentationView, P : BasePresenter<V>> : DaggerActivity() {

    @Inject
    protected lateinit var presenterFactory: PresenterFactory<V, P>

    protected lateinit var presenter: P

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = runCatching { lastNonConfigurationInstance as P }.getOrDefault(presenterFactory.get())
        try { presenter.bingView(this as V) }
        catch (e: ClassCastException) {
            throw RuntimeException(
                "The `view` provided does not implement the PresentationView interface " +
                        "expected by " + presenter.javaClass.simpleName + ".", e
            )
        }
    }

    override fun onRetainNonConfigurationInstance(): Any = presenter

    override fun onDestroy() {
        super.onDestroy()
        presenter.unbindView()
    }

}