package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.utils.Resource

interface TracksRepository {
    fun searchTracks(expression: String): Resource<List<Track>>

    //    fun addTrackToFavorites(track: Track)
//    fun removeTrackFromFavorites(track: Track)
    fun putHistory(tracks: MutableList<Track>)
    fun getHistory(): ArrayList<Track>
    fun clearHistory()
}