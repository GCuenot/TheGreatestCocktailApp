package fr.isen.guillaume.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import fr.isen.guillaume.thegreatestcocktailapp.ui.CocktailNavigationBar
import fr.isen.guillaume.thegreatestcocktailapp.ui.Screen
import fr.isen.guillaume.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class RandomActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheGreatestCocktailAppTheme(dynamicColor = false) {
                RandomCocktailScreen(
                    bottomBar = { CocktailNavigationBar(this, Screen.Random) }
                )
            }
        }
    }
}
