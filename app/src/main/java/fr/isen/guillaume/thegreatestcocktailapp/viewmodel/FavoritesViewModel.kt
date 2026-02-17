package fr.isen.guillaume.thegreatestcocktailapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.FavoritesManager
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel : ViewModel() {

    private val _favoriteCocktails = MutableStateFlow<List<CocktailDetail>>(emptyList())
    val favoriteCocktails: StateFlow<List<CocktailDetail>> = _favoriteCocktails

    fun fetchFavoriteCocktails(context: Context) {
        viewModelScope.launch {
            _favoriteCocktails.value = emptyList() // Clear the list to show loading
            val favoriteIds = FavoritesManager.getFavoriteIds(context)
            val cocktails = mutableListOf<CocktailDetail>()
            for (id in favoriteIds) {
                try {
                    val response = RetrofitClient.apiService.getCocktailDetail(id)
                    response.details.firstOrNull()?.let {
                        cocktails.add(it)
                    }
                } catch (e: Exception) {
                    // Handle error for a single cocktail fetch
                }
            }
            _favoriteCocktails.value = cocktails
        }
    }
}