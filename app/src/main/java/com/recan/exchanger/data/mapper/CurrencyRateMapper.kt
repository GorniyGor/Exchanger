package com.recan.exchanger.data.mapper

import com.recan.exchanger.data.response.CurrencyRateResponse
import com.recan.exchanger.domain.entity.Currency
import com.recan.exchanger.domain.entity.CurrencyRate

object CurrencyRateMapper {
    fun CurrencyRateResponse.toEntity(): CurrencyRate =
        CurrencyRate(
            baseCurrency,
            rates.map { Currency(it.key, it.value) }
        )
}