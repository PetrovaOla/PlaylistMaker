package petrova.ola.playlistmaker.media.playlist.domain.db

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistRepository {
    // добавление трека в плейлист
    suspend fun addTrack(track: Track, playlist: Playlist)

    // добавление плейлиста
    suspend fun addPlaylist(playlist: Playlist)

    // удаление плейлиста
    suspend fun deletePlaylist(playlist: Playlist)

    // получения списка со всеми треками
    fun getPlaylist(): Flow<List<Playlist>>

//    Обновление
    suspend fun updatePlaylist()

    suspend fun saveFile(uri: Uri): Uri
    suspend fun loadFile()
    suspend fun deleteFile(uri: Uri)
}


