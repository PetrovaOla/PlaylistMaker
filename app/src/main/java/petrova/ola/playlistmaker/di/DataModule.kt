package petrova.ola.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import petrova.ola.playlistmaker.player.data.PlayerDataImpl
import petrova.ola.playlistmaker.player.domain.PlayerData
import petrova.ola.playlistmaker.search.data.NetworkClient
import petrova.ola.playlistmaker.search.data.network.ApiService
import petrova.ola.playlistmaker.search.data.network.RetrofitNetworkClient
import petrova.ola.playlistmaker.search.data.repository.GsonBundleCodec
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.sharing.data.LocalStorage
import petrova.ola.playlistmaker.utils.GlideImageLoader
import petrova.ola.playlistmaker.utils.ImageLoader
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single(named("baseUrl")) {
        "https://itunes.apple.com"
    }

    single<ApiService> {
        Retrofit.Builder()
            .baseUrl(
                get<String>(named("baseUrl"))
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<LocalStorage> {
        LocalStorage(get())
    }

    factory { MediaPlayer() }

    factory<PlayerData> {
        PlayerDataImpl(get())
    }


    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

//    val service : Service by inject(qualifier = named("default"))

    single<GsonBundleCodec<Track>> {
        GsonBundleCodec(
            Track::class.java
        )
    }

    single<GsonBundleCodec<MutableList<*>>>(named("list")) {
        GsonBundleCodec(
            MutableList::class.java
        )
    }

    single<ImageLoader> {
        GlideImageLoader()
    }

}