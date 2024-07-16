package com.example.currencyconverter.api

import com.example.currencyconverter.model.CurrencyRate
import retrofit2.Retrofit
import retrofit2.create

object ExchangeRateService {
    private val retrofit: Retrofit = RetrofitInstance.getRetrofitInstance()

    fun getExchangeRateApi(): ExchangeRateApi {
        return retrofit.create(ExchangeRateApi::class.java)
    }
}
