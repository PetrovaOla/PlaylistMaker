package petrova.ola.playlistmaker.root.db

import androidx.room.Database
import androidx.room.RoomDatabase
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistDao
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity

import petrova.ola.playlistmaker.player.data.db.entity.TrackDao
import petrova.ola.playlistmaker.player.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}