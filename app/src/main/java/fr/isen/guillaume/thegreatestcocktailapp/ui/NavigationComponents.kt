package fr.isen.guillaume.thegreatestcocktailapp.ui

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import fr.isen.guillaume.thegreatestcocktailapp.CategoriesActivity
import fr.isen.guillaume.thegreatestcocktailapp.FavoritesActivity
import fr.isen.guillaume.thegreatestcocktailapp.RandomActivity
import fr.isen.guillaume.thegreatestcocktailapp.SearchActivity

sealed class Screen(val route: String, val label: String, val icon: ImageVector, val activityClass: Class<*>) {
    object Random : Screen("random", "Random", Icons.Default.Refresh, RandomActivity::class.java)
    object Search : Screen("search", "Search", Icons.Default.Search, SearchActivity::class.java)
    object List : Screen("list", "List", Icons.AutoMirrored.Filled.List, CategoriesActivity::class.java)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite, FavoritesActivity::class.java)

    companion object {
        fun fromRoute(route: String?): Screen? {
            return when (route) {
                "random" -> Random
                "search" -> Search
                "list" -> List
                "favorites" -> Favorites
                else -> null
            }
        }
    }
}

@Composable
fun CocktailNavigationBar(context: Context, currentScreen: Screen?) {
    val items = listOf(Screen.Random, Screen.Search, Screen.List, Screen.Favorites)
    NavigationBar {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = screen == currentScreen,
                onClick = {
                    if (screen != currentScreen) {
                        val intent = Intent(context, screen.activityClass)
                        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                        context.startActivity(intent)
                    }
                }
            )
        }
    }
}
