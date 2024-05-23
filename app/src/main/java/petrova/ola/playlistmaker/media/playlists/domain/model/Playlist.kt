package petrova.ola.playlistmaker.media.playlists.domain.model

import java.io.Serializable

data class Playlist(
    var id: Long,
    val name: String,
    val description: String?,
    var img: String?,
    val trackIds: List<Long>
): Serializable