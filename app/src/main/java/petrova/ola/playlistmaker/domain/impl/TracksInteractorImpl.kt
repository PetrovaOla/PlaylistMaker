package petrova.ola.playlistmaker.domain.impl

import petrova.ola.playlistmaker.domain.api.TracksInteractor
import petrova.ola.playlistmaker.domain.api.TracksRepository
import java.io.IOException
import java.util.concurrent.Executors

class TracksInteractorImpl(
    private val repository: TracksRepository
) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val trackResponse = repository.searchTracks(expression)
                consumer.consume(trackResponse)
            } catch (_: IOException) {
                consumer.onFailure()
            }
        }
    }
}