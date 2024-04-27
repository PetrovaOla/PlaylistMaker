package petrova.ola.playlistmaker.search.domain.api

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.domain.model.Track

interface TracksRepository {
    fun searchTracks(expression: String): Flow<Resource<List<Track>>>

    //    fun addTrackToFavorites(track: Track)
//    fun removeTrackFromFavorites(track: Track)
    fun putHistory(tracks: MutableList<Track>)
    fun getHistory(): ArrayList<Track>
    fun clearHistory()
}