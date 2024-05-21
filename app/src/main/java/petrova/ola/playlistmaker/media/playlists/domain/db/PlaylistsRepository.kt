package petrova.ola.playlistmaker.media.playlists.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistsRepository {

    // добавление трека в плейлист
    suspend fun addTrack(track: Track, playlist: Playlist):Boolean

    // добавление плейлиста
    suspend fun createPlaylist(playlist: Playlist)

    // удаление трека
    suspend fun deleteTrack(playlistId: Long, track: Track)

    // удаление плейлиста
    suspend fun deletePlaylist(playlist: Playlist)

    // получения списков плейлистов
    fun getPlaylist(): Flow<List<Playlist>>

        // получения списков плейлистов
    fun getTracks(playlistId: Long): Flow<List<Long>>


    suspend fun saveFile(inputFile: String): Uri


    suspend fun deleteFile(uri: Uri)

    suspend fun updatePlaylists() {}
}

