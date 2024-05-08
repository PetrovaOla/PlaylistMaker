package petrova.ola.playlistmaker.media.favorite.domain.impl

import kotlinx.coroutines.flow.Flow
import petrova.ola.playlistmaker.media.favorite.domain.db.FavouritesInteractor
import petrova.ola.playlistmaker.media.favorite.domain.db.FavouritesTrackRepository
import petrova.ola.playlistmaker.search.domain.model.Track

class FavouritesInteractorImpl(private val favouritesTrackRepository: FavouritesTrackRepository) :
    FavouritesInteractor {

    override suspend fun insertFavoritesTrack(track: Track) {
        return favouritesTrackRepository.insertFavoritesTrack(track)
    }

    override suspend fun deleteFavoritesTrack(track: Track) {
        return favouritesTrackRepository.deleteFavoritesTrack(track)
    }

    override fun getFavoritesTrack(): Flow<List<Track>> {
        return favouritesTrackRepository.getFavoritesTrack()
    }

}