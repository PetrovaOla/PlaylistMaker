package petrova.ola.playlistmaker.search.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.player.data.db.converters.DbConvertorPlayer
import petrova.ola.playlistmaker.root.db.AppDatabase
import petrova.ola.playlistmaker.search.data.NetworkClient
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.data.dto.TrackSearchRequest
import petrova.ola.playlistmaker.search.data.dto.TrackSearchResponse
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.sharing.data.LocalStorage

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val localStorage: LocalStorage,
    private val appDatabase: AppDatabase,
    private val dbConvertorPlayer: DbConvertorPlayer,
) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.searchTrack(TrackSearchRequest(expression))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR_CONNECT))
            }

            200 -> {

                val stored = localStorage.getHistory()
                val tracksIdFavourites = appDatabase.trackDao().getTracksIdToFavourites()
                if ((response as TrackSearchResponse).results.isEmpty()) {
                    emit(Resource.Error(ERROR_EMPTY))
                } else {
                    val data = Resource.Success(
                        response.results.map {
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
                                previewUrl = it.previewUrl,
                                isFavorite = it.isFavorite
                            )
                        }
                    )
                    verifyTrack(data.data, tracksIdFavourites)
                    emit(data)
                }

            }

            else -> {
                Log.d(
                    "TrackListLoader",
                    "Failed load track list (http code ${response.resultCode})"
                )
                emit(Resource.Error(ERROR_SERVER))

            }

        }

    }

    private fun verifyTrack(
        tracks: List<Track>?,
        tracksIdFavourites: List<Int>
    ) {
        tracksIdFavourites.forEach { tracksIdFavourites ->
            tracks?.forEach { track ->
                if (tracksIdFavourites == track.trackId) {
                    track.isFavorite = true

                }
            }
        }
    }

    override fun putHistory(tracks: MutableList<Track>) {
        localStorage.putHistory(tracks)
    }

    override fun getHistory(): ArrayList<Track> {
        return localStorage.getHistory()
    }

    override fun clearHistory() {
        localStorage.clearHistory()
    }

    companion object {
        const val ERROR_SERVER = 0
        const val ERROR_CONNECT = 1
        const val ERROR_EMPTY = 2
    }
}