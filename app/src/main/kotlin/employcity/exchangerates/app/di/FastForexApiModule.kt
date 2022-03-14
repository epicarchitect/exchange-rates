package employcity.exchangerates.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import employcity.exchangerates.app.api.FastForexApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FastForexApiModule {

    @Provides
    @Singleton
    fun provideFastForexApi() = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder().apply {
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

                addInterceptor {
                    it.proceed(
                        it.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .url(
                                it.request().url.newBuilder()
                                    .addQueryParameter("api_key", API_KEY)
                                    .build()
                            ).build()
                    )
                }
            }.build()
        )
        .build()
        .create(FastForexApi::class.java)

    companion object {
        private const val BASE_URL = "https://api.fastforex.io/"
        private const val API_KEY = "fed792137c-ffeec3952c-r8l5vp"
    }
}