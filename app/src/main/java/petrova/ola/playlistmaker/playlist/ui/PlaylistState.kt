package petrova.ola.playlistmaker.playlist.ui

import petrova.ola.playlistmaker.search.domain.model.Track

sealed interface PlaylistState {
    data class Content(
        val tracks: List<Track>
    ) : PlaylistState
    data object Empty : PlaylistState
}