package petrova.ola.playlistmaker.presentation.search

import petrova.ola.playlistmaker.domain.models.Track

sealed class SearchScreenState {
    data object Loading : SearchScreenState()
    data class trackList(val resultsList: List<Track>) : SearchScreenState()
    data class historyTrack(val historyList: List<Track>) : SearchScreenState()
    data object Error : SearchScreenState()
}