package fr.isen.guillaume.thegreatestcocktailapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import fr.isen.guillaume.thegreatestcocktailapp.viewmodel.DrinksViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrinksScreen(
    categoryName: String,
    navController: NavController,
    drinksViewModel: DrinksViewModel = viewModel()
) {
    val drinks by drinksViewModel.drinks.collectAsState()

    LaunchedEffect(categoryName) {
        drinksViewModel.fetchDrinks(categoryName)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Drinks for $categoryName") })
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            if (drinks == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(drinks!!.drinks) { drink ->
                        DrinkItem(drink = drink.name) {
                            navController.navigate("detail/${drink.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrinkItem(drink: String, onDrinkClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onDrinkClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = drink)
        }
    }
}