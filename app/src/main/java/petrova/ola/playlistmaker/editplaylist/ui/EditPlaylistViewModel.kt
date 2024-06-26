package petrova.ola.playlistmaker.editplaylist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist

class EditPlaylistViewModel(
    private val playlistsInteractor: PlaylistsInteractor,
    private val playlist: Playlist
) : ViewModel() {

    init {
        getPlaylist()
    }

    fun getPlaylist() {
        viewModelScope.launch {
            playlistsInteractor.getTracks(playlistId = playlist.id)
        }
    }

    fun updatePlaylist(image: String, name: String, description: String): Boolean {
        if (playlist.img != image || playlist.name != name || playlist.description != description) {
            viewModelScope.launch {
                runBlocking {
                    val newUri: String? = when (image) {
                        playlist.img -> image
                        "" -> null
                        else -> {
                            playlistsInteractor.deleteFile(playlist.img)
                            try {
                                playlistsInteractor.saveFile(uri = image).toString()
                            } catch (e: Exception) {
                                null
                            }
                        }
                    }

                    playlistsInteractor.updatePlaylist(
                        Playlist(
                            id = playlist.id,
                            name = name,
                            description = description,
                            img = newUri,
                            trackIds = playlist.trackIds
                        )
                    )
                }

            }
            return true
        } else return false
    }
}
