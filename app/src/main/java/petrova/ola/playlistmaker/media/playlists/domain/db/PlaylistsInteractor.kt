package petrova.ola.playlistmaker.media.playlists.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistsInteractor {
    suspend fun addTrack(track: Track, playlist: Playlist): Boolean

    suspend fun createPlaylist(playList: Playlist)

    suspend fun deleteTrack(playlistId: Long, track: Track)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getTracks(playlistId: Long): Flow<List<Long>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun saveFile(uri: String): Uri

    suspend fun deleteFile(uri: String?): Boolean

    suspend fun updatePlaylist(playlist: Playlist) {}

    suspend fun getPlaylist(id: Long): Flow<Playlist>
}