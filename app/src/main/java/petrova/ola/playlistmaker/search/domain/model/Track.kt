package petrova.ola.playlistmaker.search.domain.model

import petrova.ola.playlistmaker.utils.msToTime
import java.io.Serializable

data class Track(
    var trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String?,  //Ссылка на трек
    var isFavorite: Boolean = false
) : Serializable {
    val bigImg: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val date: String
        get() = releaseDate?.substringBefore('-').toString()
    val trackTime: String
        get() = msToTime(trackTimeMillis)
}