package employcity.exchangerates.app.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import employcity.exchangerates.app.repository.CurrenciesRepositoryImpl
import employcity.exchangerates.repository.CurrenciesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Binds
    @Singleton
    abstract fun bindCurrenciesRepository(impl: CurrenciesRepositoryImpl): CurrenciesRepository

}