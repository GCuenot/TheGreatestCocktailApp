package fr.isen.guillaume.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.guillaume.thegreatestcocktailapp.ui.CocktailNavigationBar
import fr.isen.guillaume.thegreatestcocktailapp.ui.Screen
import fr.isen.guillaume.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val drinkId = intent.getStringExtra("drinkId") ?: ""
        val fromRoute = intent.getStringExtra("from")
        val currentScreen = Screen.fromRoute(fromRoute)

        setContent {
            TheGreatestCocktailAppTheme(dynamicColor = false) {
                DetailCocktailScreen(
                    drinkId = drinkId,
                    bottomBar = { CocktailNavigationBar(this, currentScreen) }
                )
            }
        }
    }
}
