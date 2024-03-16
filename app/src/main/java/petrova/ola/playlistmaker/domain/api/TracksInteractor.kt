package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun getHistory(): List<Track>
    fun clearHistory()
    fun putHistory(tracks: MutableList<Track>)


    interface TracksConsumer {
        fun consume(trackResponse: List<Track>?)
        fun onFailure(errorMessage: String?)
    }

}