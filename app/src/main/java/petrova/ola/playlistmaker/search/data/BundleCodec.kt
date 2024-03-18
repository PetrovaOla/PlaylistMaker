package petrova.ola.playlistmaker.search.data

interface BundleCodec<T> {
    fun encodeData(toEncode: T): String
    fun decodeData(encoded: String): T
}