package petrova.ola.playlistmaker.media.playlist.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistInteractor
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistRepository
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistInteractImpl(private val repository: PlaylistRepository) : PlaylistInteractor {

    override suspend fun addTrack(track: Track, playlist: Playlist) {
        repository.addTrack(track = track, playlist = playlist)
    }

    override suspend fun addPlaylist(playList: Playlist) {
        repository.addPlaylist(playList)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        repository.getPlaylist()
    }

    override suspend fun updatePlayLists() {
        repository.updatePlaylist()
    }

    override suspend fun saveFile(uri: Uri): Uri {
        return repository.saveFile(uri)
    }

    override suspend fun loadFile() {
        repository.loadFile()
    }

    override suspend fun deleteFile(uri: Uri) {
        repository.deleteFile(uri)
    }
}
