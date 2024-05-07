package petrova.ola.playlistmaker.player.data.db.entity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    suspend fun getTracksToFavourites(): List<TrackEntity>

    @Query("SELECT id FROM track_table")
    suspend fun getTracksIdToFavourites(): List<Int>

    @Delete(entity = TrackEntity::class)
    fun deleteTrack(trackEntity: TrackEntity)
}