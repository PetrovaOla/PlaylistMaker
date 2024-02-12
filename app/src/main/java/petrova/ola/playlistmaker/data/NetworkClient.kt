package petrova.ola.playlistmaker.data

import petrova.ola.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}