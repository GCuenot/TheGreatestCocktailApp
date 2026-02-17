package fr.isen.guillaume.thegreatestcocktailapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.isen.guillaume.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheGreatestCocktailAppTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            BottomAppBar {
                IconButton(onClick = { navController.navigate("random") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Refresh, contentDescription = "Random")
                }
                IconButton(onClick = { navController.navigate("list") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List")
                }
                IconButton(onClick = { navController.navigate("favorites") }, modifier = Modifier.weight(1f)) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorites")
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "list", modifier = Modifier.padding(innerPadding)) {
            composable("random") { RandomCocktailScreen() }
            composable("list") { CategoriesScreen(navController = navController) }
            composable("favorites") { FavoritesScreen(navController = navController) }
            composable(
                "drinks/{categoryName}",
                arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
                DrinksScreen(categoryName = categoryName, navController = navController)
            }
            composable(
                "detail/{drinkId}",
                arguments = listOf(navArgument("drinkId") { type = NavType.StringType })
            ) { backStackEntry ->
                val drinkId = backStackEntry.arguments?.getString("drinkId") ?: ""
                DetailCocktailScreen(drinkId = drinkId)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TheGreatestCocktailAppTheme {
        MainScreen()
    }
}