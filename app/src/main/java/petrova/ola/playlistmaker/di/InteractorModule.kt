package petrova.ola.playlistmaker.di


import org.koin.dsl.module
import petrova.ola.playlistmaker.player.domain.PlayerInteractor
import petrova.ola.playlistmaker.player.domain.db.FavouritesInteractor
import petrova.ola.playlistmaker.player.domain.impl.FavouritesInteractorImpl
import petrova.ola.playlistmaker.player.domain.impl.PlayerInteractorImpl
import petrova.ola.playlistmaker.search.domain.api.TracksInteractor
import petrova.ola.playlistmaker.search.domain.impl.TracksInteractorImpl
import petrova.ola.playlistmaker.setting.domain.SettingInteractor
import petrova.ola.playlistmaker.setting.domain.SettingInteractorImpl
import petrova.ola.playlistmaker.sharing.domain.SharingInteractor
import petrova.ola.playlistmaker.sharing.domain.SharingInteractorImpl

val interactorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    factory<SettingInteractor> {
        SettingInteractorImpl(get())
    }

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavouritesInteractor> {
        FavouritesInteractorImpl(get())
    }

}