package employcity.exchangerates.app.api

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.*


interface FastForexApi {

    @GET("fetch-all")
    suspend fun fetchAll(@Query("from") from: String): Response<FetchAllResult>

    @GET("currencies")
    suspend fun getCurrencies(): Response<CurrenciesResult>

    data class FetchAllResult(
        @SerializedName("base")
        val base: String,
        @SerializedName("results")
        val results: Map<String, Double>,
        @SerializedName("updated")
        val updated: String,
        @SerializedName("ms")
        val ms: Int
    )

    data class CurrenciesResult(
        @SerializedName("currencies")
        val currencies: Map<String, String>,
        @SerializedName("ms")
        val ms: Int
    )
}