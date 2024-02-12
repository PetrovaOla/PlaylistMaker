package petrova.ola.playlistmaker.model

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Long, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, //Название альбома
    val releaseDate: String,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String,  //Ссылка на трек
)