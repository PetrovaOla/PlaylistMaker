package petrova.ola.playlistmaker.playlist.domain.api

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistRepository {
    fun getTrackIds(trackIds: List<Long>): Flow<Resource<List<Track>>>
}