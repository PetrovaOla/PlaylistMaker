package petrova.ola.playlistmaker.data

interface BundleCodec<T> {
    fun encodeData(toEncode: T): String
    fun decodeData(encoded: String): T
}