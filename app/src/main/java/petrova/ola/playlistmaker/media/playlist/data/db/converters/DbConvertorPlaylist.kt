package petrova.ola.playlistmaker.media.playlist.data.db.converters

import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistWithTracks
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist


class DbConvertorPlaylist {

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            description = playlist.description,
            name = playlist.name,
            img = playlist.img.toString(),
        )
    }

    fun mapToModel(relation: PlaylistWithTracks): Playlist {
        return Playlist(
            id = relation.playlist.id,
            description = relation.playlist.description,
            name = relation.playlist.name,
            img = relation.playlist.img,
            trackIds = relation.tracks.map {
                it.trackId
            }
        )
    }
}