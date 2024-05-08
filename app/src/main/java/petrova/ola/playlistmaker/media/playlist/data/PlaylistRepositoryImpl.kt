package petrova.ola.playlistmaker.media.playlist.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistRepository
import petrova.ola.playlistmaker.media.playlist.data.db.converters.DbConvertorPlaylist
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.root.db.AppDatabase
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertorPlaylist,
) : PlaylistRepository {
    override suspend fun addTrack(track: Track, playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun addPlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        TODO("Not yet implemented")
    }

    override fun getPlaylist(): Flow<List<Playlist>> {
        TODO("Not yet implemented")
    }

    override suspend fun updatePlaylist() {
        TODO("Not yet implemented")
    }

    override suspend fun saveFile(uri: Uri): Uri {
        TODO("Not yet implemented")
    }

    override suspend fun loadFile() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFile(uri: Uri) {
        TODO("Not yet implemented")
    }

}