package com.recan.exchanger.presentation.model.mapper

import com.recan.exchanger.domain.entity.Currency
import com.recan.exchanger.presentation.model.CurrencyModel

object CurrencyMapper {

    fun Currency.toModel(isBaseCurrency: Boolean = false): CurrencyModel =
        CurrencyModel(name, rate, isBaseCurrency)

    fun List<Currency>.mapToModel(): List<CurrencyModel> =
        mapIndexed { index, currency ->
            if (index == 0) currency.toModel(true) else currency.toModel()
        }

}