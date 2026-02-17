package fr.isen.guillaume.thegreatestcocktailapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.isen.guillaume.thegreatestcocktailapp.model.CocktailDetail
import fr.isen.guillaume.thegreatestcocktailapp.model.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailCocktailViewModel : ViewModel() {
    private val _cocktailDetail = MutableStateFlow<CocktailDetail?>(null)
    val cocktailDetail: StateFlow<CocktailDetail?> = _cocktailDetail

    fun fetchCocktailDetail(id: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getCocktailDetail(id)
                _cocktailDetail.value = response.details.firstOrNull()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}