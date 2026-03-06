package fr.isen.guillaume.thegreatestcocktailapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import fr.isen.guillaume.thegreatestcocktailapp.model.Drink
import fr.isen.guillaume.thegreatestcocktailapp.model.DrinksResponse
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksScreen(
    categoryName: String,
    navController: NavController
) {
    val decodedCategoryName = URLDecoder.decode(categoryName, StandardCharsets.UTF_8.toString())
    var drinksResponse by remember { mutableStateOf<DrinksResponse?>(null) }

    LaunchedEffect(decodedCategoryName) {
        try {
            drinksResponse = RetrofitClient.apiService.getDrinksByCategory(decodedCategoryName)
        } catch (e: Exception) {
            // Handle error
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Drinks for $decodedCategoryName") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (drinksResponse == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                val drinks = drinksResponse?.drinks ?: emptyList()
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(drinks) { drink ->
                        DrinkItem(drink = drink) {
                            navController.navigate("detail/${drink.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkItem(drink: Drink, onDrinkClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onDrinkClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = drink.thumbUrl,
                contentDescription = "Image of ${drink.name}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = drink.name,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
        }
    }
}
