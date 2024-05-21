package petrova.ola.playlistmaker.search.data

import petrova.ola.playlistmaker.search.data.dto.Response
import petrova.ola.playlistmaker.playlist.data.dto.TrackByIdRequest
import petrova.ola.playlistmaker.search.data.dto.TrackSearchRequest

interface NetworkClient {
    //    fun doRequest(dto: Any): Response
    suspend fun searchTrack(dto: TrackSearchRequest): Response
    suspend fun getTracks(dto: TrackByIdRequest): Response
}