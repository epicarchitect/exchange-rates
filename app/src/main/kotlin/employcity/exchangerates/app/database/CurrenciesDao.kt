package employcity.exchangerates.app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrenciesDao {

    @Insert
    suspend fun insertEntities(list: List<CurrencyEntity>)

    @Query("DELETE FROM currencies")
    suspend fun deleteEntities()

    @Query("SELECT * FROM currencies")
    fun entitiesFlow(): Flow<List<CurrencyEntity>>

    @Query("UPDATE currencies SET isFavorite = :isFavorite WHERE code = :code")
    suspend fun updateFavoriteEntity(code: String, isFavorite: Boolean)

    @Query("SELECT code FROM currencies WHERE isFavorite = :isFavorite")
    suspend fun getFavoriteCodes(isFavorite: Boolean): List<String>

}