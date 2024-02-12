package petrova.ola.playlistmaker.domain.models

data class TrackResponse(
    val result: List<Track>,
    val resultCode: Int
)