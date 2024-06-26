package petrova.ola.playlistmaker.utils

import com.google.gson.Gson
import petrova.ola.playlistmaker.search.data.BundleCodec

class GsonBundleCodec<T>(
    private val type: Class<T>
) : BundleCodec<T> {
    private val gson = Gson()

    override fun encodeData(toEncode: T): String = gson.toJson(toEncode)

    override fun decodeData(encoded: String): T = gson.fromJson(
        encoded, type
    )
}