package employcity.exchangerates.usecase

import employcity.exchangerates.repository.CurrenciesRepository

class BaseCurrencyCodeFlowUseCase(
    private val currenciesRepository: CurrenciesRepository
) {

    operator fun invoke() = currenciesRepository.baseCurrencyCodeFlow()

}