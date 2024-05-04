package petrova.ola.playlistmaker.search.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.domain.api.TracksInteractor
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.search.domain.model.Track

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.searchTracks(expression).map { result ->
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

    override fun getHistory() = repository.getHistory()

    override fun clearHistory() = repository.clearHistory()

    override fun putHistory(tracks: MutableList<Track>) = repository.putHistory(tracks)

}