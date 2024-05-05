package petrova.ola.playlistmaker.media.ui.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.player.domain.db.FavouritesInteractor
import petrova.ola.playlistmaker.player.ui.PlayerScreenState

class FavoritesViewModel(
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {
    private val stateMutableLiveData = MutableLiveData<FavoritesState>()
    private val favoriteScreenState: LiveData<FavoritesState> = stateMutableLiveData
    fun getScreenStateLiveData() = favoriteScreenState

    init {
        loadFavoriteTracks()
    }

    fun loadFavoriteTracks() {
        renderState(FavoritesState.Loading)
        viewModelScope.launch {
            favouritesInteractor
                .getFavoritesTrack()
                .collect {
                    if (it.isNotEmpty())
                        renderState(FavoritesState.Content(it))
                    else
                        renderState(FavoritesState.Empty)
                }

        }

    }


    private fun renderState(state: FavoritesState) {
        stateMutableLiveData.postValue(state)
    }
}