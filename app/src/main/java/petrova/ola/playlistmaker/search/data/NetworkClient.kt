package petrova.ola.playlistmaker.search.data

import petrova.ola.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}