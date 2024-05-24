package petrova.ola.playlistmaker.playlist.data.dto

import petrova.ola.playlistmaker.search.data.network.NetworkRequest

data class TrackByIdRequest(
    val ids: List<Long>
) : NetworkRequest
