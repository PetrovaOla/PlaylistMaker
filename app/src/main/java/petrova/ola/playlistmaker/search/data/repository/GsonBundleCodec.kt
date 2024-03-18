package petrova.ola.playlistmaker.search.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.search.data.BundleCodec

class GsonBundleCodec<T>(
    private val type: TypeToken<T>
) : BundleCodec<T> {
    private val gson = Gson()

    override fun encodeData(toEncode: T): String = gson.toJson(toEncode)

    override fun decodeData(encoded: String): T = gson.fromJson(
        encoded, type
    )
}