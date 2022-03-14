package employcity.exchangerates.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import employcity.exchangerates.app.api.RequestErrorType
import employcity.exchangerates.app.api.RequestErrorResolver
import employcity.exchangerates.data.Currency
import employcity.exchangerates.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrenciesViewModel @Inject constructor(
    currenciesFlowUseCase: CurrenciesFlowUseCase,
    baseCurrencyCodeFlowUseCase: BaseCurrencyCodeFlowUseCase,
    private val fetchCurrenciesUseCase: FetchCurrenciesUseCase,
    private val changeBaseCurrencyCodeUseCase: ChangeBaseCurrencyCodeUseCase,
    private val changeFavoriteCurrencyUseCase: ChangeFavoriteCurrencyUseCase,
    private val requestErrorResolver: RequestErrorResolver
) : ViewModel() {

    private val mutableState = MutableStateFlow(
        State(
            currencies = emptyList(),
            fetchError = null,
            filter = Filter(
                shouldShowOnlyFavorites = false,
                sortType = SortType.BY_ALPHABET,
                availableSortTypes = emptyList(),
                availableCurrenciesForBase = emptyList(),
                baseCurrency = null
            ),
            isLoading = true
        )
    )

    private val mutableIsLoading = MutableStateFlow(false)
    private val mutableShouldShowOnlyFavorites = MutableStateFlow(false)
    private val mutableSortType = MutableStateFlow(SortType.BY_ALPHABET)
    private val mutableFetchError = MutableStateFlow<RequestErrorType?>(null)
    val state = mutableState.asStateFlow()

    init {
        combine(
            currenciesFlowUseCase(),
            baseCurrencyCodeFlowUseCase(),
            mutableShouldShowOnlyFavorites,
            mutableSortType
        ) { currencies, baseCurrencyCode, showOnlyFavorites, sortType ->
            mutableState.update {
                State(
                    currencies = currencies.let {
                        if (showOnlyFavorites) it.filter { it.isFavorite }
                        else it
                    }.let {
                        when (sortType) {
                            SortType.BY_ALPHABET -> it.sortedBy { it.name }
                            SortType.BY_ALPHABET_DESC -> it.sortedByDescending { it.name }
                            SortType.BY_VALUE -> it.sortedBy { it.value }
                            SortType.BY_VALUE_DESC -> it.sortedByDescending { it.value }
                        }
                    },
                    filter = Filter(
                        shouldShowOnlyFavorites = showOnlyFavorites,
                        baseCurrency = currencies.firstOrNull { it.code == baseCurrencyCode },
                        sortType = sortType,
                        availableCurrenciesForBase = currencies.filter {
                            it.code != baseCurrencyCode
                        },
                        availableSortTypes = SortType.values().filter {
                            it != sortType
                        }
                    ),
                    isLoading = mutableIsLoading.value,
                    fetchError = mutableFetchError.value
                )
            }
        }.launchIn(viewModelScope)

        mutableIsLoading.onEach { isLoading ->
            mutableState.update {
                it.copy(
                    isLoading = isLoading
                )
            }
        }.launchIn(viewModelScope)

        mutableFetchError.onEach { error ->
            mutableState.update {
                it.copy(
                    fetchError = error
                )
            }
        }.launchIn(viewModelScope)

        refresh()
    }

    fun setFavoriteCurrency(
        code: String,
        isFavorite: Boolean
    ) = viewModelScope.launch {
        changeFavoriteCurrencyUseCase(code, isFavorite)
    }

    fun setBaseCurrencyCode(code: String) = viewModelScope.launch {
        changeBaseCurrencyCodeUseCase(code)
        refresh()
    }

    fun setShouldShowOnlyFavorites(should: Boolean) {
        mutableShouldShowOnlyFavorites.value = should
    }

    fun setSortType(type: SortType) {
        mutableSortType.value = type
    }

    fun refresh() = viewModelScope.launch {
        mutableIsLoading.value = true
        try {
            fetchCurrenciesUseCase()
            mutableFetchError.value = null
        } catch (t: Throwable) {
            mutableFetchError.value = requestErrorResolver.resolve(t)
        }
        mutableIsLoading.value = false
    }

    data class Filter(
        val shouldShowOnlyFavorites: Boolean,
        val baseCurrency: Currency?,
        val sortType: SortType,
        val availableCurrenciesForBase: List<Currency>,
        val availableSortTypes: List<SortType>
    )

    enum class SortType {
        BY_ALPHABET,
        BY_ALPHABET_DESC,
        BY_VALUE,
        BY_VALUE_DESC,
    }

    data class State(
        val currencies: List<Currency>,
        val fetchError: RequestErrorType?,
        val filter: Filter,
        val isLoading: Boolean
    )
}