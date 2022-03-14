package employcity.exchangerates.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import employcity.exchangerates.repository.CurrenciesRepository
import employcity.exchangerates.usecase.*

@Module
@InstallIn(SingletonComponent::class)
class UseCasesModule {

    @Provides
    fun provideFetchCurrenciesUseCase(
        currenciesRepository: CurrenciesRepository
    ) = FetchCurrenciesUseCase(currenciesRepository)

    @Provides
    fun provideBaseCurrencyCodeFlowUseCase(
        currenciesRepository: CurrenciesRepository
    ) = BaseCurrencyCodeFlowUseCase(currenciesRepository)

    @Provides
    fun provideCurrenciesUseCaseFlow(
        currenciesRepository: CurrenciesRepository
    ) = CurrenciesFlowUseCase(currenciesRepository)

    @Provides
    fun provideChangeBaseCodeAndFetchUseCase(
        currenciesRepository: CurrenciesRepository
    ) = ChangeBaseCurrencyCodeUseCase(currenciesRepository)

    @Provides
    fun provideChangeFavoriteCurrencyUseCase(
        currenciesRepository: CurrenciesRepository
    ) = ChangeFavoriteCurrencyUseCase(currenciesRepository)

}