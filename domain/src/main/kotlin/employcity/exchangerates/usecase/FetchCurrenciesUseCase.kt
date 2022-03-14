package employcity.exchangerates.usecase

import employcity.exchangerates.repository.CurrenciesRepository

class FetchCurrenciesUseCase(
    private val currenciesRepository: CurrenciesRepository
) {

    suspend operator fun invoke() = currenciesRepository.fetchCurrencies()

}