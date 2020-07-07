package com.recan.exchanger.domain.entity

data class CurrencyRate(
    val baseCurrency: String,
    val rates: List<Currency>
)

data class Currency(
    val name: String,
    val rate: Double
)