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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.isen.guillaume.thegreatestcocktailapp.ui.theme.TheGreatestCocktailAppTheme

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Random : Screen("random", "Random", Icons.Default.Refresh)
    object Search : Screen("search", "Search", Icons.Default.Search)
    object List : Screen("list", "List", Icons.AutoMirrored.Filled.List)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            TheGreatestCocktailAppTheme(dynamicColor = false) { // Disabled dynamic color
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Random, Screen.Search, Screen.List, Screen.Favorites)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    val isSelected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = isSelected,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                // Restore state only if not reselecting the same item
                                restoreState = !isSelected
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = Screen.List.route, modifier = Modifier.padding(innerPadding)) {
            composable(Screen.Random.route) { RandomCocktailScreen() }
            composable(Screen.Search.route) { SearchScreen(navController = navController) }
            composable(Screen.List.route) { CategoriesScreen(navController = navController) }
            composable(Screen.Favorites.route) { FavoritesScreen(navController = navController) }
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
