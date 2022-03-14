package employcity.exchangerates.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import employcity.exchangerates.app.api.RequestErrorResolver
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ResolversModule {

    @Provides
    @Singleton
    fun provideRequestErrorResolver() = RequestErrorResolver()

}