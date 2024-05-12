package petrova.ola.playlistmaker.media.playlist.ui

import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist

sealed interface PlaylistState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistState


    data object Empty : PlaylistState
}