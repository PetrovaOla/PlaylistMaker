package petrova.ola.playlistmaker.media.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistInteractor

class PlaylistViewModel(
    private val playlistsInteractor: PlaylistInteractor
) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistState>()
    private val playlistsScreenState: LiveData<PlaylistState> = stateMutableLiveData
    fun getScreenStateLiveData() = playlistsScreenState

    init {
        loadPlaylist()
    }

    fun loadPlaylist() {
        viewModelScope.launch {
            playlistsInteractor
                .getPlaylists()
                .collect {
                    if (it.isNotEmpty())
                        renderState(PlaylistState.Content(it))
                    else
                        renderState(PlaylistState.Empty)
                }

        }
    }
    private fun renderState(state: PlaylistState) {
        stateMutableLiveData.postValue(state)
    }

}