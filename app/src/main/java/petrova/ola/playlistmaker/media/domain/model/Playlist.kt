package petrova.ola.playlistmaker.media.domain.model

import petrova.ola.playlistmaker.search.domain.model.Track

data class Playlist(
    val name: String,
    val list: List<Track>,
    val count: Int,
    val img:String,
)