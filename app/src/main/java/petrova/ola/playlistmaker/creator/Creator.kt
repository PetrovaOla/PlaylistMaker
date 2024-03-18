package petrova.ola.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.player.domain.PlayerInteractor
import petrova.ola.playlistmaker.player.domain.PlayerInteractorImpl
import petrova.ola.playlistmaker.search.data.BundleCodec
import petrova.ola.playlistmaker.search.data.network.RetrofitNetworkClient
import petrova.ola.playlistmaker.search.data.repository.GsonBundleCodec
import petrova.ola.playlistmaker.search.data.repository.TracksRepositoryImpl
import petrova.ola.playlistmaker.search.domain.api.TracksInteractor
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.search.domain.impl.TracksInteractorImpl
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.setting.data.SettingRepositoryImpl
import petrova.ola.playlistmaker.setting.domain.SettingInteractor
import petrova.ola.playlistmaker.setting.domain.SettingInteractorImpl
import petrova.ola.playlistmaker.sharing.data.LocalStorage
import petrova.ola.playlistmaker.sharing.domain.SharingInteractor
import petrova.ola.playlistmaker.sharing.domain.SharingInteractorImpl
import petrova.ola.playlistmaker.utils.GlideImageLoader
import petrova.ola.playlistmaker.utils.ImageLoader
import petrova.ola.playlistmaker.utils.Utils.Companion.PREFERENCES


object Creator {
    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            RetrofitNetworkClient(context),
            LocalStorage(context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)),
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(
            getTracksRepository(context)
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

    fun provideSettingInteractor(context: Context): SettingInteractor {
        val sharedPreferences = provideSharedPreferences(context)
        return SettingInteractorImpl(
            settingRepository = SettingRepositoryImpl(sharedPreferences)
        )
    }

    private fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, AppCompatActivity.MODE_PRIVATE)
    }

    fun providePlayerInteractor(url: String): PlayerInteractor {
        return PlayerInteractorImpl(url = url)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(context = context)
    }
}