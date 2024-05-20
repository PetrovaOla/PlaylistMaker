package petrova.ola.playlistmaker.media.favorite.ui

import petrova.ola.playlistmaker.search.domain.model.Track

sealed interface FavoritesState {

    data object Loading : FavoritesState

    data class Content(
        val favoritesList: List<Track>
    ) : FavoritesState


    data object Empty : FavoritesState
}