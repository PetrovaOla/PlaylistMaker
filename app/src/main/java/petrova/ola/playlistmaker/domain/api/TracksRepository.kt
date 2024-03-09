package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): List<Track>
}