package fr.isen.guillaume.thegreatestcocktailapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RandomCocktailViewModel : ViewModel() {
    private val _cocktailDetail = MutableStateFlow<CocktailDetail?>(null)
    val cocktailDetail: StateFlow<CocktailDetail?> = _cocktailDetail

    init {
        fetchRandomCocktail()
    }

    fun fetchRandomCocktail() {
        viewModelScope.launch {
            _cocktailDetail.value = null // Show loading
            try {
                val response = RetrofitClient.apiService.getRandomCocktail()
                _cocktailDetail.value = response.details.firstOrNull()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}