package petrova.ola.playlistmaker.media.playlists.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "playlist_tracks_table")
data class PlaylistTrackEntity(
    @PrimaryKey(autoGenerate = true) val rowId: Long,
    @ColumnInfo("track_id") val trackId: Long,
    @ColumnInfo("playlist_id") val playlistId: Long,
)


data class PlaylistWithTracks(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "playlist_id",
        entity = PlaylistTrackEntity::class
    )
    val tracks: List<PlaylistTrackEntity>
)