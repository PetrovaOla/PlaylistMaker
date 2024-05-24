package petrova.ola.playlistmaker.di

import org.koin.dsl.module
import petrova.ola.playlistmaker.media.favorite.data.FavouritesTrackRepositoryImpl
import petrova.ola.playlistmaker.media.favorite.domain.db.FavouritesTrackRepository
import petrova.ola.playlistmaker.media.playlists.data.PlaylistsRepositoryImpl
import petrova.ola.playlistmaker.media.playlists.data.db.converters.DbConvertorPlaylist
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import petrova.ola.playlistmaker.player.data.db.converters.DbConvertorPlayer
import petrova.ola.playlistmaker.playlist.data.repository.PlaylistRepositoryImpl
import petrova.ola.playlistmaker.playlist.domain.api.PlaylistRepository
import petrova.ola.playlistmaker.root.db.AppDatabase
import petrova.ola.playlistmaker.search.data.repository.TracksRepositoryImpl
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.setting.data.SettingRepositoryImpl
import petrova.ola.playlistmaker.setting.domain.SettingRepository

val repositoryModule = module {

    single<SettingRepository> {
        SettingRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get(), get())
    }

    factory { DbConvertorPlaylist() }

    factory { DbConvertorPlayer() }

    single<FavouritesTrackRepository> {
        FavouritesTrackRepositoryImpl(get(), get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get<AppDatabase>().playlistDao(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
}