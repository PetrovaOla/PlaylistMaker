package petrova.ola.playlistmaker.media.playlist.domain.model

import petrova.ola.playlistmaker.search.domain.model.Track

data class Playlist(
    var id: Long,
    val name: String,
    val description: String?,
    var img: String?,
    val trackIds: List<Long>
)