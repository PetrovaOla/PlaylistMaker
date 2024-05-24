package petrova.ola.playlistmaker.playlist.domain.api

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.domain.model.Track

interface PlaylistInteractor {
    fun getTrackIds(trackIds: List<Long>): Flow<Pair<List<Track>?, Int?>>

}