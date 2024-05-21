package petrova.ola.playlistmaker.playlist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.playlist.domain.api.PlaylistInteractor
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistViewModel(
    private val playlist: Playlist,
    private val networkInteractor: PlaylistInteractor,
    private val databaseInteractor: PlaylistsInteractor
) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistState>()
    private val playlistsScreenState: LiveData<PlaylistState> = stateMutableLiveData
    fun getScreenStateLiveData() = playlistsScreenState

    private val timeMutableLiveData = MutableLiveData<Int>()
    val timeLiveData: LiveData<Int> = timeMutableLiveData

    init {
        loadTracks()
    }

    fun loadTracks() {
        viewModelScope.launch {
            databaseInteractor.getTracks(playlist.id)
                .collect { trackIds ->
                    if (trackIds.isNotEmpty())
                        networkInteractor.getTrackIds(trackIds).collect { tracks ->
                            if (tracks.first.isNullOrEmpty()) {
                                renderState(PlaylistState.Empty)
                            } else
                                renderState(PlaylistState.Content(tracks.first!!))
                        }
                    else
                        renderState(PlaylistState.Empty)
                }

        }
    }

    fun trackTime() {
        viewModelScope.launch {
            val result = databaseInteractor.getTracks(playlist.id)
                .map { trackIds ->
                    if (trackIds.isNotEmpty()) {
                        networkInteractor.getTrackIds(trackIds).map {
                            if (it.first.isNullOrEmpty())
                                listOf(0)
                            else
                                it.first!!.map { it.trackTimeMillis }
                        }.firstOrNull()?.reduceOrNull { a, b -> a + b } ?: 0
                    } else
                        0
                }.first()
            timeMutableLiveData.postValue(result)
        }

    }

    fun deleteTrack(track: Track) {
        viewModelScope.launch {
            databaseInteractor.deleteTrack(playlist.id, track = track)
            loadTracks()
        }
    }

    private fun renderState(state: PlaylistState) {
        stateMutableLiveData.postValue(state)
    }

}