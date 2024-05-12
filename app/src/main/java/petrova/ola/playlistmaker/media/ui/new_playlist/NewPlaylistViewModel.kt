package petrova.ola.playlistmaker.media.ui.new_playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistInteractor
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist

class NewPlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    fun createPlaylist(image: String, name: String, description: String) {
        viewModelScope.launch {
            runBlocking {
                val newUri: String? = if (image.isNotBlank())
                    playlistInteractor.saveFile(uri = image).toString()
                else
                    null
                playlistInteractor.createPlaylist(
                    Playlist(
                        name = name,
                        description = description,
                        img = newUri,
                        id = 0L,
                        count = 0,
                        list = mutableListOf()
                    )
                )
            }
        }
    }
}
