package petrova.ola.playlistmaker.media.playlists.ui

import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist

sealed interface PlaylistsState {
    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState


    data object Empty : PlaylistsState
}