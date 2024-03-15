package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(trackResponse: List<Track>?, errorMessage: String?)
        fun onFailure()
    }

}