package fr.isen.guillaume.thegreatestcocktailapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.FavoritesManager
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomCocktailScreen(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit = {}
) {
    var cocktailDetail by remember { mutableStateOf<CocktailDetail?>(null) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var isFavorite by remember(cocktailDetail) {
        mutableStateOf(cocktailDetail?.id?.let { FavoritesManager.isFavorite(context, it) } ?: false)
    }

    fun fetchRandom() {
        scope.launch {
            cocktailDetail = null
            try {
                cocktailDetail = RetrofitClient.apiService.getRandomCocktail().details.firstOrNull()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    LaunchedEffect(Unit) {
        fetchRandom()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(cocktailDetail?.name ?: "Random Cocktail") },
                actions = {
                    cocktailDetail?.let { detail ->
                        IconButton(onClick = {
                            if (isFavorite) {
                                FavoritesManager.removeFavorite(context, detail.id)
                            } else {
                                FavoritesManager.addFavorite(context, detail.id)
                            }
                            isFavorite = !isFavorite
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    if (isFavorite) "Added to favorites" else "Removed from favorites"
                                )
                            }
                        }) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                    IconButton(onClick = { fetchRandom() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Get new random cocktail")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (cocktailDetail == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                CocktailDetailContent(cocktailDetail!!)
            }
        }
    }
}
