package petrova.ola.playlistmaker.playlist.ui

import android.util.Log
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
import petrova.ola.playlistmaker.sharing.domain.SharingInteractor
import kotlin.math.roundToInt

class PlaylistViewModel(
    private var playlist: Playlist,
    private val networkInteractor: PlaylistInteractor,
    private val databaseInteractor: PlaylistsInteractor,
    private val sharingInteractor: SharingInteractor,
) : ViewModel() {

    private val stateMutableLiveData = MutableLiveData<PlaylistState>()
    private val playlistsScreenState: LiveData<PlaylistState> = stateMutableLiveData
    fun getScreenStateLiveData() = playlistsScreenState

    private val timeMutableLiveData = MutableLiveData<Int>()
    val timeLiveData: LiveData<Int> = timeMutableLiveData

    private val mutablePlaylistReload = MutableLiveData<Playlist>()
    val playlistReload: LiveData<Playlist> = mutablePlaylistReload

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

    fun deletePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            databaseInteractor.deletePlaylist(playlist)
        }
    }


    private fun renderState(state: PlaylistState) {
        stateMutableLiveData.postValue(state)
    }

    fun sharePlaylist() {
        sharingInteractor.sharePlaylist(message = shareMessage())
    }

    private fun shareMessage(): String {
        val builder = StringBuilder()
            .appendLine(playlist.name)
            .appendLine(playlist.description)
            .append("Количество треков: ")
            .appendLine(playlist.trackIds.size)

        if (playlistsScreenState.value is PlaylistState.Empty) {
            builder.appendLine()
            builder.append("Треков нет")
        } else {
            val tracks = (playlistsScreenState.value as PlaylistState.Content).tracks
            tracks.forEachIndexed { index, track ->
                builder.appendLine()
                builder.append(index + 1)
                builder.append(". ")
                builder.append(track.artistName)
                builder.append(" - ")
                builder.append(track.trackName)
                builder.append(" (")
                val seconds = (track.trackTimeMillis.toDouble() / 1000).roundToInt()
                builder.append(seconds / 60)
                builder.append(":")
                builder.append(seconds % 60)
                builder.append(")")
            }
        }

        return builder.toString()
    }

    fun requestPlaylistUpdate() {
        viewModelScope.launch {
            val first = databaseInteractor.getPlaylist(playlist.id).firstOrNull() ?: return@launch
            mutablePlaylistReload.postValue(first)
            playlist = first
        }
    }

}