package petrova.ola.playlistmaker.playlist.data.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.player.data.db.converters.DbConvertorPlayer
import petrova.ola.playlistmaker.playlist.data.dto.GetTrackResponse
import petrova.ola.playlistmaker.playlist.data.dto.TrackByIdRequest
import petrova.ola.playlistmaker.playlist.domain.api.PlaylistRepository
import petrova.ola.playlistmaker.root.db.AppDatabase
import petrova.ola.playlistmaker.search.data.NetworkClient
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.data.dto.TrackSearchRequest
import petrova.ola.playlistmaker.search.data.dto.TrackSearchResponse
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.sharing.data.LocalStorage

class PlaylistRepositoryImpl(
    private val networkClient: NetworkClient,
    private val dbConvertorPlayer: DbConvertorPlayer,
) : PlaylistRepository {
    override fun getTrackIds(trackIds: List<Long>): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.getTracks(TrackByIdRequest(trackIds))
        when (response.resultCode) {
            -1 -> {
                emit(Resource.Error(ERROR_CONNECT))
            }

            200 -> {


                if ((response as GetTrackResponse).results.isEmpty()) {
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





    companion object {
        const val ERROR_SERVER = 0
        const val ERROR_CONNECT = 1
        const val ERROR_EMPTY = 2
    }
}