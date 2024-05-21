package petrova.ola.playlistmaker.root.db

import androidx.room.Database
import androidx.room.RoomDatabase
import petrova.ola.playlistmaker.media.playlists.data.db.entities.PlaylistDao
import petrova.ola.playlistmaker.media.playlists.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlists.data.db.entities.PlaylistTrackEntity

import petrova.ola.playlistmaker.player.data.db.entity.TrackDao
import petrova.ola.playlistmaker.player.data.db.entity.TrackEntity

@Database(
//    version = 2,
    version = 1,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class],
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trackDao(): TrackDao
    abstract fun playlistDao(): PlaylistDao
}