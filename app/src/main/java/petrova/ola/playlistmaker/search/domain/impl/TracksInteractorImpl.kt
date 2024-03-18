package petrova.ola.playlistmaker.search.domain.impl

import petrova.ola.playlistmaker.search.data.Resource
import petrova.ola.playlistmaker.search.domain.api.TracksInteractor
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.search.domain.model.Track
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {

            when (val resourse = repository.searchTracks(expression)) {
                is Resource.Success -> {
                    consumer.consume(resourse.data)
                }

                is Resource.Error -> {
                    consumer.onFailure(resourse.message)
                }
            }

        }
    }

    override fun getHistory() = repository.getHistory()

    override fun clearHistory() = repository.clearHistory()

    override fun putHistory(tracks: MutableList<Track>) = repository.putHistory(tracks)

}