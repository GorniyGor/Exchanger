package com.recan.exchanger.data

import com.recan.exchanger.data.response.CurrencyRateResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangerApi {
    @GET("api/android/latest")
    fun getRates(@Query("base") currency: String): Observable<CurrencyRateResponse>
}