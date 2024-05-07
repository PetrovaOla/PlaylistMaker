package petrova.ola.playlistmaker.player.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import petrova.ola.playlistmaker.player.data.converters.DbConvertor
import petrova.ola.playlistmaker.player.data.db.entity.AppDatabase
import petrova.ola.playlistmaker.player.data.db.entity.TrackEntity
import petrova.ola.playlistmaker.player.domain.db.FavouritesTrackRepository
import petrova.ola.playlistmaker.search.domain.model.Track

class FavouritesTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val dbConvertor: DbConvertor,
) : FavouritesTrackRepository {

    override suspend fun insertFavoritesTrack(track: Track) {
        val tracksId = appDatabase.trackDao().getTracksIdToFavourites()
        if (!tracksId.contains(track.trackId)) {
            val trackEntity = dbConvertor.mapToEntity(track)
            appDatabase.trackDao().insertTrack(trackEntity)
        }
    }

    override suspend fun deleteFavoritesTrack(track: Track) {
        val trackEntity = dbConvertor.mapToEntity(track)
        appDatabase.trackDao().deleteTrack(trackEntity)
    }

    override fun getFavoritesTrack(): Flow<List<Track>> = flow {
        val tracks = appDatabase.trackDao().getTracksToFavourites()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> dbConvertor.mapToModel(track) }
    }


}