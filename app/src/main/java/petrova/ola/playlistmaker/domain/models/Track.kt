package petrova.ola.playlistmaker.domain.models

import petrova.ola.playlistmaker.utils.msToTime

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, //Название альбома
    val releaseDate: String,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String,  //Ссылка на трек
) {
    val bigImg: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val date: String
        get() = releaseDate.substringBefore('-')
    val trackTime: String?
        get() = msToTime(trackTimeMillis)
}