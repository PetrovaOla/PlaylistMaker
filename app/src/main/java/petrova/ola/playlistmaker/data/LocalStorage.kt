package petrova.ola.playlistmaker.data

import android.content.SharedPreferences
import com.google.gson.Gson
import petrova.ola.playlistmaker.domain.models.Track

class LocalStorage(private val sharedPreferences: SharedPreferences) {

    private fun changeFavorites(trackId: String, remove: Boolean) {
        val mutableSet = getSavedFavorites().toMutableSet()
        val modified = if (remove) mutableSet.remove(trackId) else mutableSet.add(trackId)
        if (modified) sharedPreferences.edit().putStringSet(FAVORITES_KEY, mutableSet).apply()
    }

    fun addToFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = false)
    }

    fun removeFromFavorites(trackId: String) {
        changeFavorites(trackId = trackId, remove = true)
    }

    fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_SHARED).apply()
    }

    fun getSavedFavorites(): Set<String> {
        return sharedPreferences.getStringSet(FAVORITES_KEY, emptySet()) ?: emptySet()
    }

    fun putHistory(tracks: MutableList<Track>) {
        sharedPreferences.edit().putString(
            SEARCH_HISTORY_SHARED, Gson().toJson(tracks)
        ).apply()
    }

    fun getHistory(): ArrayList<Track> {
        return ArrayList(
            Gson().fromJson(
                sharedPreferences.getString(SEARCH_HISTORY_SHARED, "[]"),
                Array<Track>::class.java
            ).toList()
        )
    }


    private companion object {
        const val SEARCH_HISTORY_SHARED = "search_history_shared"
        const val FAVORITES_KEY = "FAVORITES_KEY"
    }
}
