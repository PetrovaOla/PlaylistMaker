package petrova.ola.playlistmaker.media.playlist.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistInteractor
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistRepository
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistInteractImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addTrack(track: Track, playlistId: Long): Boolean {
        return repository.addTrack(track, playlistId)
    }

    override suspend fun createPlaylist(playList: Playlist) {
        repository.createPlaylist(playList)
    }

    override suspend fun deleteTrack(playlistId: Long, track: Track) {
        repository.deleteTrack(playlistId, track)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getTracks(playlistId: Long): Flow<List<Track>> = flow {
        repository.getTracks(playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> = flow {
        repository.getPlaylist()
    }


    override suspend fun saveFile(uri: String, playlistId: Long): Uri {
        return repository.saveFile(uri, playlistId)
    }

    override suspend fun loadFile() {
        repository.loadFile()
    }

    override suspend fun deleteFile(uri: Uri) {
        repository.deleteFile(uri)
    }
}
