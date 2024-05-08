package petrova.ola.playlistmaker.media.playlist.data.db.converters

import androidx.core.net.toUri
import com.google.gson.Gson
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.media.playlist.domain.model.TrackList


class DbConvertorPlaylist {

    private val gson = Gson()

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            list = gson.toJson(playlist.list),
            description = playlist.description,
            name = playlist.name,
            img = playlist.img.toString(),
            count = playlist.count
        )
    }

    fun mapToModel(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            list = gson.fromJson(playlist.list, TrackList::class.java),
            description = playlist.description,
            name = playlist.name,
            img = playlist.img?.toUri(),
            count = playlist.count
        )
    }
}