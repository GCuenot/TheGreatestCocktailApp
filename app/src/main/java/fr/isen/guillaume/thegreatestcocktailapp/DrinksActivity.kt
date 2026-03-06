package fr.isen.guillaume.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import fr.isen.guillaume.thegreatestcocktailapp.ui.CocktailNavigationBar
import fr.isen.guillaume.thegreatestcocktailapp.ui.Screen
import fr.isen.guillaume.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class DrinksActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categoryName = intent.getStringExtra("categoryName") ?: ""
        setContent {
            TheGreatestCocktailAppTheme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { CocktailNavigationBar(this, Screen.List) }
                ) { innerPadding ->
                    DrinksScreen(
                        categoryName = categoryName,
                        modifier = Modifier.padding(innerPadding),
                        context = this
                    )
                }
            }
        }
    }
}
