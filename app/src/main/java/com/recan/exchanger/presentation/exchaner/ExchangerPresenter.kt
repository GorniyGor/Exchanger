package com.recan.exchanger.presentation.exchaner

import com.recan.exchanger.di.FlowScope
import com.recan.exchanger.domain.usecases.GetExchangerRatesUseCase
import com.recan.exchanger.presentation.Utils.Optional
import com.recan.exchanger.presentation.base.BasePresenter
import com.recan.exchanger.presentation.base.PresentationView
import com.recan.exchanger.presentation.model.CurrencyModel
import com.recan.exchanger.presentation.model.mapper.CurrencyMapper.mapToModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

typealias CurrencyAndValue = Pair<String, Double>
typealias BinaryOperator<T> = BiFunction<T, T, T>

@FlowScope
class ExchangerPresenter @Inject constructor(
    private val getRates: GetExchangerRatesUseCase
) : BasePresenter<ExchangerPresenter.ExchangerView>() {

    private val onCurrencyChangedObservable =
        BehaviorSubject.createDefault( Optional<CurrencyAndValue>())
    private val onValueChangesObservable =
        BehaviorSubject.createDefault( Optional<CurrencyAndValue>())

    fun loadData() {
        view?.setLoading(true)

        Observable.combineLatest(
            onCurrencyChangedObservable,
            onValueChangesObservable,
            BinaryOperator<Optional<CurrencyAndValue>> { base, changed ->
                getActualCurrencyValue(base, changed)
            }
        )
            .switchMap {
                val rates = if(!it.isPresent) getRates()
                else getRates(it.value!!.first, it.value!!.second)

                rates.map { it.mapToModel() }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    view?.setLoading(false)
                    view?.hideError()
                    view?.updateData(it)
                },
                {
                    view?.setLoading(false)
                    view?.showError(it)
                }
            ).clearAtTime()

    }

    private fun getActualCurrencyValue(
        base: Optional<CurrencyAndValue>, changed: Optional<CurrencyAndValue>
    ): Optional<CurrencyAndValue> {
        return if(base.isPresent && changed.isPresent) {
            val value =
                if(base.value!!.first == changed.value!!.first) changed.value!!.second
                else base.value!!.second

            Optional(base.value!!.first to value)
        } else if(base.isPresent) base
        else changed
    }

    fun onCurrencySelected(currency: String, value: Double) =
        onCurrencyChangedObservable.onNext(Optional(currency to value))

    fun onCurrencyAmountChange(currency: String, value: Double) =
        onValueChangesObservable.onNext(Optional(currency to value))


    interface ExchangerView : PresentationView {
        fun updateData(list: List<CurrencyModel>)
        fun showError(throwable: Throwable)
        fun hideError()
        fun setLoading(isShowing: Boolean)
    }
}