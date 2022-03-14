package employcity.exchangerates.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import employcity.exchangerates.app.screens.CurrenciesScreen
import employcity.exchangerates.app.theme.ExchangeRatesTheme


@AndroidEntryPoint
class AppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExchangeRatesTheme {
                CurrenciesScreen()
            }
        }
    }
}