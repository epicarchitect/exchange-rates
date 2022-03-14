package employcity.exchangerates.app.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class CurrencyEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val code: String,
    val name: String,
    val value: Double,
    val isFavorite: Boolean
)