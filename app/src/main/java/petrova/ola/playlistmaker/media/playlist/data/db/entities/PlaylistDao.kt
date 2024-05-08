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
    suspend fun createPl(playlist: PlaylistEntity):Long

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePl(playList: PlaylistEntity)

    @Query("UPDATE playlist_table SET list =:tracks, count=:count WHERE id=:playlistId")
    suspend fun updateTracks(playlistId: Long, tracks: String, count: Int)

    @Insert(entity = Track::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrackToPl(track: Track)

    @Query("SELECT list FROM playlist_table WHERE id=:playlistId")
    suspend fun getTracks(playlistId: Long): List<String>

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePl(playList: PlaylistEntity)

    @Delete(entity = Track::class)
    fun deleteTrackFromPl(track: Track)
}