package com.recan.exchanger.data.response

data class CurrencyRateResponse(
    val baseCurrency: String,
    val rates: Map<String, Double>
)