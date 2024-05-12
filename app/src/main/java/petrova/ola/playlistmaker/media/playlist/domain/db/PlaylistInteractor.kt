package petrova.ola.playlistmaker.media.playlist.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistInteractor {

    suspend fun addTrack(track: Track, playlistId: Long): Boolean

    suspend fun createPlaylist(playList: Playlist)

    suspend fun deleteTrack(playlistId: Long, track: Track)

    suspend fun deletePlaylist(playlist: Playlist)

    fun getTracks(playlistId: Long): Flow<List<Track>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun saveFile(uri: String): Uri

    suspend fun deleteFile(uri: Uri)

    suspend fun updatePlaylists() {}
}