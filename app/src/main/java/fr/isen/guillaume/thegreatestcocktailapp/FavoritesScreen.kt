package fr.isen.guillaume.thegreatestcocktailapp

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.FavoritesManager
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import fr.isen.guillaume.thegreatestcocktailapp.ui.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    context: Context,
    bottomBar: @Composable () -> Unit = {}
) {
    var favoriteCocktails by remember { mutableStateOf<List<CocktailDetail>?>(null) }

    LaunchedEffect(Unit) {
        val favoriteIds = FavoritesManager.getFavoriteIds(context)
        if (favoriteIds.isEmpty()) {
            favoriteCocktails = emptyList()
        } else {
            try {
                val cocktails = favoriteIds.mapNotNull { id ->
                    RetrofitClient.apiService.getCocktailDetail(id).details.firstOrNull()
                }
                favoriteCocktails = cocktails
            } catch (e: Exception) {
                favoriteCocktails = emptyList()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text("Favorite Cocktails") })
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                favoriteCocktails == null -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                favoriteCocktails!!.isEmpty() -> {
                    Text(
                        "Your favorite cocktails will appear here.",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(favoriteCocktails!!) { cocktail ->
                            FavoriteItem(cocktail = cocktail) {
                                val intent = Intent(context, DetailActivity::class.java).apply {
                                    putExtra("drinkId", cocktail.id)
                                    putExtra("from", Screen.Favorites.route)
                                }
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteItem(cocktail: CocktailDetail, onCocktailClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onCocktailClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cocktail.thumbUrl,
                contentDescription = "Cocktail Image",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = cocktail.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(text = cocktail.category, fontSize = 14.sp)
            }
        }
    }
}
