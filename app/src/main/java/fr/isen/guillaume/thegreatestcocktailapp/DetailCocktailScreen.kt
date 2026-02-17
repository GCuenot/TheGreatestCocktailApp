package fr.isen.guillaume.thegreatestcocktailapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.FavoritesManager
import fr.isen.guillaume.thegreatestcocktailapp.viewmodel.DetailCocktailViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailCocktailScreen(
    drinkId: String,
    detailViewModel: DetailCocktailViewModel = viewModel()
) {
    val cocktailDetail by detailViewModel.cocktailDetail.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(drinkId) {
        detailViewModel.fetchCocktailDetail(drinkId)
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var isFavorite by remember(cocktailDetail) {
        mutableStateOf(cocktailDetail?.id?.let { FavoritesManager.isFavorite(context, it) } ?: false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(cocktailDetail?.name ?: "Cocktail Details") },
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
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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

@Composable
fun CocktailDetailContent(cocktail: CocktailDetail) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            AsyncImage(
                model = cocktail.thumbUrl,
                contentDescription = "Cocktail Image",
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(text = cocktail.name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = "Category: ${cocktail.category}")
            Text(text = "Glass: ${cocktail.glass}")
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Ingredients", fontWeight = FontWeight.Bold)
                    cocktail.getIngredients().forEach {
                        Text(text = "${it.first} - ${it.second}")
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Recipe", fontWeight = FontWeight.Bold)
                    Text(text = cocktail.instructions)
                }
            }
        }
    }
}