package petrova.ola.playlistmaker.player.domain.db

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.search.domain.model.Track

interface FavouritesTrackRepository {
// добавление трека в избранное
    suspend fun insertFavoritesTrack(track: Track)
// удаление трека из избранного
    suspend fun deleteFavoritesTrack(track: Track)
// получения списка со всеми треками, добавленными в избранное
    suspend fun getFavoritesTrack(): Flow<List<Track>>
}