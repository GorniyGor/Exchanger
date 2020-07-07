package com.recan.exchanger.presentation

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

object Utils {
    val exchangeRateFormat =
        DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.FRANCE))
            .apply { isGroupingUsed = false }

    class Optional<T> {
        var value: T? = null
        val isPresent: Boolean
        get() = value != null

        constructor(value: T) {
            this.value = value
        }
        constructor() {}
    }

}