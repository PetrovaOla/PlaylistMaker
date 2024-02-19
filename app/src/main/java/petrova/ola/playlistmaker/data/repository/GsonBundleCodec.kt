package petrova.ola.playlistmaker.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.data.BundleCodec

class GsonBundleCodec<T> : BundleCodec<T> {
    private val gson = Gson()

    override fun <T> encodeData(toEncode: T): String = gson.toJson(toEncode)

    override fun <T> decodeData(encoded: String): T = gson.fromJson(
        encoded,
        object : TypeToken<T>() {}.type
    )
}