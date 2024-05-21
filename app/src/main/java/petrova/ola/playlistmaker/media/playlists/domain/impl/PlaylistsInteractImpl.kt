package petrova.ola.playlistmaker.media.playlists.domain.impl

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsInteractor
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistsInteractImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {

    override suspend fun addTrack(track: Track, playlist: Playlist):Boolean {
        return repository.addTrack(track, playlist)
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

    override fun getTracks(playlistId: Long): Flow<List<Long>> {
        return repository.getTracks(playlistId)
    }

    override fun getPlaylists(): Flow<List<Playlist>> {
        return repository.getPlaylist()
    }


    override suspend fun saveFile(uri: String): Uri {
        return repository.saveFile(uri)
    }

    override suspend fun deleteFile(uri: Uri) {
        repository.deleteFile(uri)
    }
}
