package petrova.ola.playlistmaker.di

import org.koin.dsl.module
import petrova.ola.playlistmaker.search.data.repository.TracksRepositoryImpl
import petrova.ola.playlistmaker.search.domain.api.TracksRepository
import petrova.ola.playlistmaker.setting.data.SettingRepositoryImpl
import petrova.ola.playlistmaker.setting.domain.SettingRepository

val repositoryModule = module {

    single<SettingRepository> {
        SettingRepositoryImpl(get())
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

}