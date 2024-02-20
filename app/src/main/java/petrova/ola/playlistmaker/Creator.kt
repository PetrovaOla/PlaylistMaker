package petrova.ola.playlistmaker

import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.data.BundleCodec
import petrova.ola.playlistmaker.data.network.RetrofitNetworkClient
import petrova.ola.playlistmaker.data.repository.GsonBundleCodec
import petrova.ola.playlistmaker.data.repository.TracksRepositoryImpl
import petrova.ola.playlistmaker.domain.api.TracksInteractor
import petrova.ola.playlistmaker.domain.api.TracksRepository
import petrova.ola.playlistmaker.domain.impl.TracksInteractorImpl
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.presentation.GlideImageLoader
import petrova.ola.playlistmaker.presentation.ImageLoader

object Creator {
    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(
            getTracksRepository()
        )
    }

    fun <T> provideBundleCodec(type: TypeToken<T>): BundleCodec<T> = GsonBundleCodec(type)
    fun provideImageLoader(): ImageLoader = GlideImageLoader()

    val bundleCodecTrack by lazy {
        provideBundleCodec(
            object : TypeToken<Track>() {}
        )
    }
    val bundleCodecTrackList by lazy {
        provideBundleCodec(
            object : TypeToken<MutableList<Track>>() {}
        )
    }
}