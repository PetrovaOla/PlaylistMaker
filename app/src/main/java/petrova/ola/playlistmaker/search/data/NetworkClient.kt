package petrova.ola.playlistmaker.search.data

import petrova.ola.playlistmaker.search.data.dto.Response
import petrova.ola.playlistmaker.search.data.network.NetworkRequest

interface NetworkClient {
    //    fun doRequest(dto: Any): Response
    suspend fun doRequestSuspend(dto: NetworkRequest): Response
}