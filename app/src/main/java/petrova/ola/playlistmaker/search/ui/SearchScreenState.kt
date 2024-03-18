package petrova.ola.playlistmaker.search.ui

import petrova.ola.playlistmaker.search.domain.model.Track

sealed class SearchScreenState {
    data object Empty : SearchScreenState()
    data object Loading : SearchScreenState()
    data class TrackList(val resultsList: List<Track>) : SearchScreenState()
    data class HistoryTracks(val historyList: List<Track>) : SearchScreenState()
    data class Error(val message: String? = null) : SearchScreenState()
}