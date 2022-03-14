package employcity.exchangerates.usecase

import employcity.exchangerates.repository.CurrenciesRepository

class ChangeBaseCurrencyCodeUseCase(
    private val currenciesRepository: CurrenciesRepository
) {

    suspend operator fun invoke(code: String) = currenciesRepository.changeBaseCurrencyCode(code)

}