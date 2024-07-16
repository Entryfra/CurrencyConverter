package com.example.currencyconverter.api

import com.example.currencyconverter.model.CurrencyRate
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    @GET("latest")
    suspend fun getLatestRates(@Query("apikey") apiKey: String): CurrencyRate
}