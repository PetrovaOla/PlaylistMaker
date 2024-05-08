package petrova.ola.playlistmaker.media.playlist.domain.model

data class Playlist(
    var id: Long,
    val name: String,
    val description: String?,
    val list: String,
    var count: Int = 0,
    var img: String?,
)