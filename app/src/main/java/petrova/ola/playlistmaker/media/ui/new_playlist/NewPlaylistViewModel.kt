package petrova.ola.playlistmaker.media.ui.new_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist

class NewPlaylistViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    fun createPlaylist(image: String, name: String, description: String) {
        viewModelScope.launch {
            runBlocking {
                val newUri: String? = if (image.isNotBlank())
                    playlistsInteractor.saveFile(uri = image).toString()
                else
                    null
                playlistsInteractor.createPlaylist(
                    Playlist(
                        name = name,
                        description = description,
                        img = newUri,
                        id = 0L,
                        trackIds = emptyList()
                    )
                )
            }
        }
    }
}
