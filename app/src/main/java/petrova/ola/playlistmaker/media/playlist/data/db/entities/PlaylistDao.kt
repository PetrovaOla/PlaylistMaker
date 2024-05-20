package petrova.ola.playlistmaker.media.playlist.data.db.entities

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylists(): List<PlaylistEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createPlaylist(playlist: PlaylistEntity): Long

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playList: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playList: PlaylistEntity)

    @Query("SELECT * FROM playlist_table WHERE id=:playlistId")
    suspend fun getTracks(playlistId: Long): List<PlaylistWithTracks>

    @Query("SELECT * FROM playlist_table")
    suspend fun getPlaylistsWithTracks(): List<PlaylistWithTracks>

    @Query(
        "INSERT INTO playlist_tracks_table" +
                " (track_id, playlist_id) VALUES " +
                "(:trackId, :playlistId)"
    )
    suspend fun addTrack(playlistId: Long, trackId: Long)

    @Query(
        """
            SELECT EXISTS (
                SELECT * FROM playlist_tracks_table WHERE
                playlist_id=:playlistId AND track_id=:trackId
            )
        """
    )
    suspend fun containsTrack(playlistId: Long, trackId: Long): Boolean

    @Query("DELETE FROM playlist_tracks_table WHERE playlist_id=:playlistId AND track_id=:trackId")
    suspend fun deleteTrack(playlistId: Long, trackId: Long)
}