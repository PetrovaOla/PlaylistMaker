package petrova.ola.playlistmaker.media.ui.favorites

import petrova.ola.playlistmaker.search.domain.model.Track

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(
        val favoritesList: List<Track>
    ) : FavoritesState


    data object Empty : FavoritesState
}