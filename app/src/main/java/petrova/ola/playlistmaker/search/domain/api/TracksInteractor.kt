package petrova.ola.playlistmaker.search.domain.api

import petrova.ola.playlistmaker.search.domain.model.Track

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