package petrova.ola.playlistmaker.search.data.network

import petrova.ola.playlistmaker.playlist.data.dto.GetTrackResponse
import petrova.ola.playlistmaker.search.data.dto.TrackSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //    Base URL: https://itunes.apple.com
    //    Для поиска песен используйте следующий GET-запрос:
    //    /search?entity=song

    @GET("/search?entity=song")
    suspend fun getTrack(@Query("term") text: String): TrackSearchResponse

    @GET("/lookup")
    suspend fun getTrackId(@Query("id") songId: Long): GetTrackResponse

    @GET("/lookup")
    suspend fun getTrackIds(@Query("id") songIds: String): GetTrackResponse
}