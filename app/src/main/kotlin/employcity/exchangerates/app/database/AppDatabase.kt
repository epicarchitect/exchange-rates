package employcity.exchangerates.app.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CurrencyEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val currenciesDao: CurrenciesDao
}