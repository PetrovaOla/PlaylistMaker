package petrova.ola.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import petrova.ola.playlistmaker.media.ui.MediaViewModel
import petrova.ola.playlistmaker.media.ui.favorites.FavoritesViewModel
import petrova.ola.playlistmaker.media.ui.playlist.PlaylistViewModel
import petrova.ola.playlistmaker.player.ui.PlayerViewModel
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchViewModel
import petrova.ola.playlistmaker.setting.ui.SettingViewModel


val viewModelModule = module {

    viewModel {
        MediaViewModel()
    }

    viewModel { (track: Track) ->
        PlayerViewModel(track = track,
            playerInteractor = get(),
            favouritesInteractor = get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        PlaylistViewModel(get())
    }

    viewModel {
        FavoritesViewModel(get())
    }


}