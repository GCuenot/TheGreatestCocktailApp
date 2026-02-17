package fr.isen.guillaume.thegreatestcocktailapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.guillaume.thegreatestcocktailapp.model.CategoriesResponse
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {
    private val _categories = MutableStateFlow<CategoriesResponse?>(null)
    val categories: StateFlow<CategoriesResponse?> = _categories

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCategories()
                _categories.value = response
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}