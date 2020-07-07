package com.recan.exchanger.presentation.model

data class CurrencyModel(
    val name: String,
    val rate: Double,
    val isBaseCurrency: Boolean
)