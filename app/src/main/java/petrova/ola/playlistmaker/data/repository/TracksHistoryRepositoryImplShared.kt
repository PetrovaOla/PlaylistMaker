package petrova.ola.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import petrova.ola.playlistmaker.domain.api.TracksHistoryRepository
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.ui.search.SearchActivity.Companion.SEARCH_HISTORY_SHARED

class TracksHistoryRepositoryImplShared(
    private val sharedPreferences: SharedPreferences
) : TracksHistoryRepository {
    override fun putHistory(tracks: MutableList<Track>) {
        sharedPreferences.edit().putString(
            SEARCH_HISTORY_SHARED, Gson().toJson(tracks)
        ).apply()
    }

    override fun getHistory(): ArrayList<Track> {
        return ArrayList(
            Gson().fromJson(
                sharedPreferences.getString(SEARCH_HISTORY_SHARED, "[]"),
                Array<Track>::class.java
            ).toList()
        )
    }

    override fun clearHistory() {
        sharedPreferences.edit().remove(SEARCH_HISTORY_SHARED).apply()
    }
}