package petrova.ola.playlistmaker.media.favorite.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.player.data.db.converters.DbConvertorPlayer
import petrova.ola.playlistmaker.root.db.AppDatabase
import petrova.ola.playlistmaker.player.data.db.entity.TrackEntity
import petrova.ola.playlistmaker.media.favorite.domain.db.FavouritesTrackRepository
import petrova.ola.playlistmaker.search.domain.model.Track

class FavouritesTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertorPlayer: DbConvertorPlayer,
) : FavouritesTrackRepository {

    override suspend fun insertFavoritesTrack(track: Track) {
        val tracksId = appDatabase.trackDao().getTracksIdToFavourites()
        if (!tracksId.contains(track.trackId)) {
            val trackEntity = dbConvertorPlayer.mapToEntity(track)
            appDatabase.trackDao().insertTrack(trackEntity)
        }
    }

    override suspend fun deleteFavoritesTrack(track: Track) {
        val trackEntity = dbConvertorPlayer.mapToEntity(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override fun getFavoritesTrack(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracksToFavourites()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> dbConvertorPlayer.mapToModel(track) }
    }


}