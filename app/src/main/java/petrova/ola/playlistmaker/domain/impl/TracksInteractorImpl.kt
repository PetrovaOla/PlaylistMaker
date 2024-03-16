package petrova.ola.playlistmaker.domain.impl

import petrova.ola.playlistmaker.domain.api.TracksInteractor
import petrova.ola.playlistmaker.domain.api.TracksRepository
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.utils.Resource
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