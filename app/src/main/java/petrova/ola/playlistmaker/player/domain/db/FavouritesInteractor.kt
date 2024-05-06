package petrova.ola.playlistmaker.player.domain.db

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.domain.model.Track

interface FavouritesInteractor {
    suspend fun insertFavoritesTrack(track: Track)
    suspend fun deleteFavoritesTrack(track: Track)
    fun getFavoritesTrack(): Flow<List<Track>>
}