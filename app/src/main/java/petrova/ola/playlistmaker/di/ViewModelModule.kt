package petrova.ola.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import petrova.ola.playlistmaker.media.ui.media.MediaViewModel
import petrova.ola.playlistmaker.media.favorite.ui.FavoritesViewModel
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.media.playlists.ui.PlaylistsViewModel
import petrova.ola.playlistmaker.media.ui.new_playlist.NewPlaylistViewModel
import petrova.ola.playlistmaker.player.ui.PlayerViewModel
import petrova.ola.playlistmaker.playlist.ui.PlaylistViewModel
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
            favouritesInteractor = get(),
            playlistsInteractor = get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        FavoritesViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(get())
    }

    viewModel { (playlist:Playlist) ->
        PlaylistViewModel(playlist = playlist,
            get(), get())
    }

}