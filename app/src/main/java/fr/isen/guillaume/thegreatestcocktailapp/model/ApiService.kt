package fr.isen.guillaume.thegreatestcocktailapp.model

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("random.php")
    suspend fun getRandomCocktail(): CocktailDetailResponse

    @GET("list.php?c=list")
    suspend fun getCategories(): CategoriesResponse

    @GET("filter.php")
    suspend fun getDrinksByCategory(@Query("c") category: String): DrinksResponse

    @GET("lookup.php")
    suspend fun getCocktailDetail(@Query("i") id: String): CocktailDetailResponse
}
