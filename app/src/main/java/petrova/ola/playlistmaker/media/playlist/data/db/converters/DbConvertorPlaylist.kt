package petrova.ola.playlistmaker.media.playlist.data.db.converters

import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist


class DbConvertorPlaylist {

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            list = playlist.list,
            description = playlist.description,
            name = playlist.name,
            img = playlist.img.toString(),
            count = playlist.count
        )
    }

    fun mapToModel(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            list = playlist.list ?: "",
            description = playlist.description,
            name = playlist.name,
            img = playlist.img,
            count = playlist.count
        )
    }
}