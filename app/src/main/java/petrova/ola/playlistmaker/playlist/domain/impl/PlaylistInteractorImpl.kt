package petrova.ola.playlistmaker.playlist.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import petrova.ola.playlistmaker.playlist.domain.api.PlaylistInteractor
import petrova.ola.playlistmaker.playlist.domain.api.PlaylistRepository
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.domain.model.Track

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {
    override fun getTrackIds(trackIds: List<Long>): Flow<Pair<List<Track>?, Int?>> {

        return repository.getTrackIds(trackIds).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }

                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }

        }

    }

}