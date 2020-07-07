package com.recan.exchanger.domain.usecases

import com.recan.exchanger.di.FlowScope
import com.recan.exchanger.domain.entity.Currency
import com.recan.exchanger.domain.repository.IExchangerRepository
import io.reactivex.Observable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@FlowScope
class GetExchangerRatesUseCase @Inject constructor (private val dataStore: IExchangerRepository) {

    companion object {
        private const val UPDATE_RATES_INTERVAL = 1L
        private const val INIT_CURRENCY = "EUR"
        private const val INIT_CURRENCY_VALUE = 1.0
    }

    operator fun invoke(currency: String? = null, value: Double? = null): Observable<List<Currency>> {
        val (baseCurrency, baseValue) = getActualParams(currency, value)
        return Observable.interval(0, UPDATE_RATES_INTERVAL, TimeUnit.SECONDS)
            .flatMap {
                dataStore.getRatesByCurrency(baseCurrency).map { currencyRate ->
                    val list = mutableListOf(Currency(baseCurrency, baseValue))
                    list.addAll(currencyRate.rates.map { it.copy(rate = it.rate * baseValue) })
                    list.toList()
                }
            }
    }

    private fun getActualParams(currency: String? = null, value: Double? = null): Pair<String, Double> {
        return if(currency != null && value != null) {
            currency to value
        } else {
            INIT_CURRENCY to INIT_CURRENCY_VALUE
        }
    }

}