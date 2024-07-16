package com.example.currencyconverter

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.currencyconverter.api.ExchangeRateService
import com.example.currencyconverter.model.CurrencyRate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextAmount: EditText
    private lateinit var spinnerFromCurrency: Spinner
    private lateinit var spinnerToCurrency: Spinner
    private lateinit var buttonConvert: Button
    private lateinit var textViewResult: TextView


    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextAmount = findViewById(R.id.editTextAmount)
        spinnerFromCurrency = findViewById(R.id.spinnerFromCurrency)
        spinnerToCurrency = findViewById(R.id.spinnerToCurrency)
        buttonConvert = findViewById(R.id.buttonConvert)
        textViewResult = findViewById(R.id.textViewResult)


        val apiKey = "fca_live_oKJpxlS6LEcBwgpCKrWaA15onQ31lFUc7tEJQlVV" //

        fetchExchangeRates(apiKey)

        buttonConvert.setOnClickListener {
            val amount = editTextAmount.text.toString().toDoubleOrNull() ?: 0.0
            val fromCurrency = spinnerFromCurrency.selectedItem.toString()
            val toCurrency = spinnerToCurrency.selectedItem.toString()

            convertCurrency(amount, fromCurrency, toCurrency, apiKey)
        }
    }

    private fun fetchExchangeRates(apiKey: String) {
        viewModelScope.launch {
            try {
                val currencyRate = withContext(Dispatchers.IO) {
                    ExchangeRateService.getExchangeRateApi().getLatestRates(apiKey)
                }
                Log.d("CurrencyConverter", "Полученные данные: $currencyRate")

                if (currencyRate != null && currencyRate.data.isNotEmpty()) {
                    // Заполняем Spinner'ы валютами
                    val currencies = currencyRate.data.keys.toList()
                    val adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, currencies)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerFromCurrency.adapter = adapter
                    spinnerToCurrency.adapter = adapter
                } else {
                    Log.e("CurrencyConverter", "Ошибка: данные о валюте недоступны")
                    Toast.makeText(this@MainActivity, "Ошибка: данные о валюте недоступны", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Ошибка получения данных", e)
                Toast.makeText(this@MainActivity, "Ошибка получения данных", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun convertCurrency(amount: Double, fromCurrency: String, toCurrency: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val currencyRate = withContext(Dispatchers.IO) {
                    ExchangeRateService.getExchangeRateApi().getLatestRates(apiKey)
                }
                Log.d("CurrencyConverter", "Данные из API: $currencyRate")

                if (currencyRate != null && currencyRate.data.isNotEmpty()) {
                    Log.d("CurrencyConverter", "fromCurrency: $fromCurrency, toCurrency: $toCurrency")
                    val convertedAmount = amount * (currencyRate.data[toCurrency]!! / currencyRate.data[fromCurrency]!!)
                    textViewResult.text = String.format("%.2f %s", convertedAmount, toCurrency)
                } else {
                    Log.e("CurrencyConverter", "Ошибка: данные о валюте недоступны")
                    Toast.makeText(this@MainActivity, "Ошибка: данные о валюте недоступны", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("CurrencyConverter", "Ошибка конвертации", e)
                Toast.makeText(this@MainActivity, "Ошибка конвертации", Toast.LENGTH_SHORT).show()
            }
        }
    }

}