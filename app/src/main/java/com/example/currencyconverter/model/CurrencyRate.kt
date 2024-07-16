package com.example.currencyconverter.model

data class CurrencyRate(
    val base: String,
    val date: String,
    val data: Map<String, Double>
)