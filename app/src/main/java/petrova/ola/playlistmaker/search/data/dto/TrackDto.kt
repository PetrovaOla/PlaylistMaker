package petrova.ola.playlistmaker.search.data.dto

data class TrackDto(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String?, //Ссылка на трек
    var isFavorite: Boolean = false
)