package petrova.ola.playlistmaker.media.playlist.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val description: String?,
    val img: String?,
)