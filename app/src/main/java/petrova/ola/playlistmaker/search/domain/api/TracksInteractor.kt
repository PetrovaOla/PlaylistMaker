package petrova.ola.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.domain.model.Track

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    fun getHistory(): List<Track>
    fun clearHistory()
    fun putHistory(tracks: MutableList<Track>)


}