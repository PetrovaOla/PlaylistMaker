package petrova.ola.playlistmaker.data.repository

import android.util.Log
import petrova.ola.playlistmaker.data.LocalStorage
import petrova.ola.playlistmaker.data.NetworkClient
import petrova.ola.playlistmaker.data.dto.TrackSearchRequest
import petrova.ola.playlistmaker.data.dto.TrackSearchResponse
import petrova.ola.playlistmaker.domain.api.TracksRepository
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.utils.Resource

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage
) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            -1 -> {
                Resource.Error("Проверьте подключение к интернету")
            }

            200 -> {
                val stored = localStorage.getHistory()
                Resource.Success((response as TrackSearchResponse).results.map {
                    Track(
                        trackId = it.trackId,
                        trackName = it.trackName,
                        artistName = it.artistName,
                        trackTimeMillis = it.trackTimeMillis,
                        artworkUrl100 = it.artworkUrl100,
                        collectionName = it.collectionName,
                        releaseDate = it.releaseDate,
                        primaryGenreName = it.primaryGenreName,
                        country = it.country,
                        previewUrl = it.previewUrl
                    )
                })
            }

            else -> {
                Log.d(
                    "TrackListLoader",
                    "Failed load track list (http code ${response.resultCode})"
                )
//                throw IOException("Failed load track list (http code ${response.resultCode})")
                Resource.Error("Ошибка сервера")

            }

        }

    }

//    override fun addTrackToFavorites(track: Track) {
//        localStorage.addToFavorites(track.trackId.toString())
//    }
//
//    override fun removeTrackFromFavorites(track: Track) {
//        localStorage.removeFromFavorites(track.trackId.toString())
//    }

    override fun putHistory(tracks: MutableList<Track>) {
        localStorage.putHistory(tracks)
    }

    override fun getHistory(): ArrayList<Track> {
        return localStorage.getHistory()
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }
}