package employcity.exchangerates.usecase

import employcity.exchangerates.repository.CurrenciesRepository

class CurrenciesFlowUseCase(
    private val currenciesRepository: CurrenciesRepository
) {

    operator fun invoke() = currenciesRepository.currenciesFlow()

}