package petrova.ola.playlistmaker.media.playlists.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsInteractor

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistsState>()
    private val playlistsScreenState: LiveData<PlaylistsState> = stateMutableLiveData
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
                        renderState(PlaylistsState.Content(it))
                    else
                        renderState(PlaylistsState.Empty)
                }

        }
    }
    private fun renderState(state: PlaylistsState) {
        stateMutableLiveData.postValue(state)
    }

}