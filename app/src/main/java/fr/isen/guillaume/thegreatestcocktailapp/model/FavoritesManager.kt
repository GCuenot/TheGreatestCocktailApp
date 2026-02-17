package fr.isen.guillaume.thegreatestcocktailapp.model

import android.content.Context
import android.content.SharedPreferences

object FavoritesManager {
    private const val PREFS_NAME = "CocktailFavorites"
    private const val FAVORITES_KEY = "favorite_ids"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getFavoriteIds(context: Context): Set<String> {
        return getPrefs(context).getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    fun addFavorite(context: Context, id: String) {
        val prefs = getPrefs(context)
        val favorites = getFavoriteIds(context).toMutableSet()
        favorites.add(id)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun removeFavorite(context: Context, id: String) {
        val prefs = getPrefs(context)
        val favorites = getFavoriteIds(context).toMutableSet()
        favorites.remove(id)
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply()
    }

    fun isFavorite(context: Context, id: String): Boolean {
        return getFavoriteIds(context).contains(id)
    }
}