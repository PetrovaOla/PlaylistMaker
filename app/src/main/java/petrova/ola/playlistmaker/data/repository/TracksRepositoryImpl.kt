package petrova.ola.playlistmaker.data.repository

import android.util.Log
import petrova.ola.playlistmaker.data.NetworkClient
import petrova.ola.playlistmaker.data.dto.TrackSearchRequest
import petrova.ola.playlistmaker.data.dto.TrackSearchResponse
import petrova.ola.playlistmaker.domain.api.TracksRepository
import petrova.ola.playlistmaker.domain.models.Track
import java.io.IOException

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {

            return (response as TrackSearchResponse).results.map {
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
            }

        } else {
            Log.d("TrackListLoader", "Failed load track list (http code ${response.resultCode})")
            throw IOException("Failed load track list (http code ${response.resultCode})")
        }
    }
}