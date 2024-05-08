package petrova.ola.playlistmaker.media.playlist.data.db.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import petrova.ola.playlistmaker.search.domain.model.Track

@Dao
interface PlaylistDao {

    @Query("SELECT * FROM playlist_table")
    suspend fun getAll(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPl(playlist: PlaylistEntity)

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePl(playList: PlaylistEntity)

    @Insert(entity = Track::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPl(track: Track)

    @Delete(entity = PlaylistEntity::class)
    fun deletePl(playList: PlaylistEntity)

    @Delete(entity = Track::class)
    fun deleteTrackFromPl(track: Track)
}