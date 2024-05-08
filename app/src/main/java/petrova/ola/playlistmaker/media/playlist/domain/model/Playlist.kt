package petrova.ola.playlistmaker.media.playlist.domain.model

import android.net.Uri

data class Playlist(
    val id: Int,
    val name: String,
    val description: String?,
    val list: TrackList,
    var count: Int = 0,
    val img: Uri?,
)