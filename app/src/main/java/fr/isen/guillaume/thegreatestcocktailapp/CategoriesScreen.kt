package fr.isen.guillaume.thegreatestcocktailapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.isen.guillaume.thegreatestcocktailapp.model.CategoriesResponse
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var categoriesResponse by remember { mutableStateOf<CategoriesResponse?>(null) }

    LaunchedEffect(Unit) {
        try {
            categoriesResponse = RetrofitClient.apiService.getCategories()
        } catch (e: Exception) {
            // Handle error
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cocktail Categories") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (categoriesResponse == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = modifier.fillMaxSize()
                ) {
                    items(categoriesResponse!!.categories) { category ->
                        CategoryItem(category = category.name) {
                            val encodedCategoryName = URLEncoder.encode(category.name, StandardCharsets.UTF_8.toString())
                            navController.navigate("drinks/$encodedCategoryName")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(category: String, onCategoryClick: () -> Unit) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onCategoryClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Text(
                text = category,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
        }
    }
}