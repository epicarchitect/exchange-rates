package employcity.exchangerates.app.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import employcity.exchangerates.app.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun providerAppDatabase(
        app: Application
    ) = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app_database"
    ).build()

}