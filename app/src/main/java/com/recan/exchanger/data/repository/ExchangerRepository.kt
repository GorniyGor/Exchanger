package com.recan.exchanger.data.repository

import com.recan.exchanger.data.ExchangerApi
import com.recan.exchanger.data.mapper.CurrencyRateMapper.toEntity
import com.recan.exchanger.domain.entity.CurrencyRate
import com.recan.exchanger.domain.repository.IExchangerRepository
import io.reactivex.Observable

class ExchangerRepository (private val api: ExchangerApi): IExchangerRepository {

    override fun getRatesByCurrency(currency: String): Observable<CurrencyRate> =
        api.getRates(currency).map { it.toEntity() }
}