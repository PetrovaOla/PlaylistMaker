package petrova.ola.playlistmaker.media.playlist.data.db.converters

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track

val gson = Gson()

class DbConvertorPlaylist {

    fun mapToEntity(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            list = gson.toJson(playlist.list ?: emptyList<Track>()),
            description = playlist.description,
            name = playlist.name,
            img = playlist.img.toString(),
            count = playlist.count
        )
    }

    fun mapToModel(playlist: PlaylistEntity): Playlist {
        val type = object : TypeToken<MutableList<Track>>() {}.type
        return Playlist(
            id = playlist.id,
            list = gson.fromJson(playlist.list, type),
            description = playlist.description,
            name = playlist.name,
            img = playlist.img,
            count = playlist.count
        )
    }
}