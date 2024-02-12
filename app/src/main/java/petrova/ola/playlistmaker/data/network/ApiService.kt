package petrova.ola.playlistmaker.data.network

import petrova.ola.playlistmaker.data.dto.TrackSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //    Base URL: https://itunes.apple.com
//    Для поиска песен используйте следующий GET-запрос:
//    /search?entity=song
//    Текст для поиска передаётся в @Query параметром term:
    @GET("/search?entity=song")
    fun getTrack(@Query("term") text: String): Call<TrackSearchResponse>



}