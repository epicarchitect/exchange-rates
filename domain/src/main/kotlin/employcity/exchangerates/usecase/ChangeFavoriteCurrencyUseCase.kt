package employcity.exchangerates.usecase

import employcity.exchangerates.repository.CurrenciesRepository

class ChangeFavoriteCurrencyUseCase(
    private val currenciesRepository: CurrenciesRepository
) {

    suspend operator fun invoke(code: String, isFavorite: Boolean) = currenciesRepository.changeFavoriteCurrency(code, isFavorite)

}