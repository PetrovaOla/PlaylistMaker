package petrova.ola.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import petrova.ola.playlistmaker.search.data.NetworkClient
import petrova.ola.playlistmaker.search.data.dto.Response
import petrova.ola.playlistmaker.playlist.data.dto.TrackByIdRequest
import petrova.ola.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitNetworkClient(
    private val apiService: ApiService,
    private val context: Context
) : NetworkClient {

    override suspend fun searchTrack(dto: TrackSearchRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTrack(dto.expression)
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    override suspend fun getTracks(dto: TrackByIdRequest): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getTrackIds(dto.ids.joinToString())
                response.apply { resultCode = 200 }
            } catch (e: Throwable) {
                Response().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}