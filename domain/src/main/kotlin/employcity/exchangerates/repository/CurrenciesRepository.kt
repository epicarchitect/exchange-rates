package employcity.exchangerates.repository

import employcity.exchangerates.data.Currency
import kotlinx.coroutines.flow.Flow

interface CurrenciesRepository {

    fun currenciesFlow(): Flow<List<Currency>>

    fun baseCurrencyCodeFlow(): Flow<String>

    suspend fun changeBaseCurrencyCode(code: String)

    suspend fun changeFavoriteCurrency(code: String, isFavorite: Boolean)

    suspend fun fetchCurrencies()

}