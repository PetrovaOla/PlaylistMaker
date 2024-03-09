package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.domain.models.Track

interface TracksHistoryRepository {
    fun putHistory(tracks: MutableList<Track>)
    fun getHistory(): ArrayList<Track>
    fun clearHistory()
}