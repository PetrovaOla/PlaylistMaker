package petrova.ola.playlistmaker.media.playlist.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistInteractor {

    suspend fun addTrack(track: Track, playlist: Playlist)

    suspend fun addPlaylist(playList: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getPlaylist():  Flow<List<Playlist>>

    suspend fun updatePlayLists()

    suspend fun saveFile(uri: Uri): Uri
    suspend fun loadFile()
    suspend fun deleteFile(uri: Uri)
}