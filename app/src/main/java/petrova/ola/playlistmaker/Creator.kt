package petrova.ola.playlistmaker

import petrova.ola.playlistmaker.data.BundleCodec
import petrova.ola.playlistmaker.data.network.RetrofitNetworkClient
import petrova.ola.playlistmaker.data.repository.GsonBundleCodec
import petrova.ola.playlistmaker.data.repository.TracksRepositoryImpl
import petrova.ola.playlistmaker.domain.api.TracksInteractor
import petrova.ola.playlistmaker.domain.api.TracksRepository
import petrova.ola.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            getTracksRepository()
        )
    }

    fun <T> provideBundleCodec(): BundleCodec<T> = GsonBundleCodec()
}