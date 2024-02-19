package petrova.ola.playlistmaker.data

interface BundleCodec<T> {
    fun <T> encodeData(toEncode: T): String
    fun <T> decodeData(encoded: String): T
}