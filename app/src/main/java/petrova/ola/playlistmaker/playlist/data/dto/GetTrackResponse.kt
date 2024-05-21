package petrova.ola.playlistmaker.playlist.data.dto

import petrova.ola.playlistmaker.search.data.dto.Response
import petrova.ola.playlistmaker.search.data.dto.TrackDto

data class GetTrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
