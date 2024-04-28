package petrova.ola.playlistmaker.search.data.dto

import petrova.ola.playlistmaker.search.data.network.NetworkRequest

data class TrackSearchRequest(
    val expression: String
) : NetworkRequest