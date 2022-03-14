package employcity.exchangerates.app.repository

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.room.withTransaction
import employcity.exchangerates.app.api.FastForexApi
import employcity.exchangerates.app.database.AppDatabase
import employcity.exchangerates.app.database.CurrencyEntity
import employcity.exchangerates.data.Currency
import employcity.exchangerates.repository.CurrenciesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrenciesRepositoryImpl @Inject constructor(
    application: Application,
    private val fastForexApi: FastForexApi,
    private val database: AppDatabase
) : CurrenciesRepository {

    private val sharedPreferences = application.getSharedPreferences("currencies", Context.MODE_PRIVATE)
    private var baseCurrencyCode: String
        get() = sharedPreferences.getString("baseCurrencyCode", "USD") ?: "USD"
        set(value) {
            sharedPreferences.edit { putString("baseCurrencyCode", value) }
            baseCurrencyCodeState.value = value
        }

    private val baseCurrencyCodeState = MutableStateFlow(baseCurrencyCode)
    private var fetchingJob: Job? = null

    override fun currenciesFlow() = database.currenciesDao.entitiesFlow().map {
        it.map {
            Currency(
                it.code,
                it.name,
                it.value,
                it.isFavorite
            )
        }
    }

    override suspend fun fetchCurrencies() {
        coroutineScope {
            if (fetchingJob?.isActive == true) {
                fetchingJob?.cancel()
            }

            fetchingJob = launch {
                database.withTransaction {
                    val currenciesJob = async {
                        checkNotNull(fastForexApi.getCurrencies().body()).currencies
                    }

                    val ratesJob = async {
                        checkNotNull(fastForexApi.fetchAll(baseCurrencyCode).body()).results
                    }

                    val favoriteCodes = database.currenciesDao.getFavoriteCodes(true)
                    database.currenciesDao.deleteEntities()

                    val currencies = currenciesJob.await()
                    val rates = ratesJob.await()

                    database.currenciesDao.insertEntities(
                        currencies.map {
                            CurrencyEntity(
                                0,
                                it.key,
                                it.value,
                                rates[it.key] ?: 0.0,
                                favoriteCodes.contains(it.key)
                            )
                        }
                    )
                }

                fetchingJob = null
            }
        }
    }

    override suspend fun changeFavoriteCurrency(
        code: String,
        isFavorite: Boolean
    ) {
        database.currenciesDao.updateFavoriteEntity(code, isFavorite)
    }

    override fun baseCurrencyCodeFlow() = baseCurrencyCodeState

    override suspend fun changeBaseCurrencyCode(code: String) {
        baseCurrencyCode = code
    }
}