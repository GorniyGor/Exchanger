package com.recan.exchanger.domain.repository

import com.recan.exchanger.domain.entity.CurrencyRate
import io.reactivex.Observable

interface IExchangerRepository {
    fun getRatesByCurrency(currency: String): Observable<CurrencyRate>
}