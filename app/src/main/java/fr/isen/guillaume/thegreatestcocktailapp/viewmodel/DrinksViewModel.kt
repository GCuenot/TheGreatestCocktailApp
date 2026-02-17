package fr.isen.guillaume.thegreatestcocktailapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.guillaume.thegreatestcocktailapp.model.DrinksResponse
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DrinksViewModel : ViewModel() {
    private val _drinks = MutableStateFlow<DrinksResponse?>(null)
    val drinks: StateFlow<DrinksResponse?> = _drinks

    fun fetchDrinks(categoryName: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getDrinksByCategory(categoryName)
                _drinks.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}