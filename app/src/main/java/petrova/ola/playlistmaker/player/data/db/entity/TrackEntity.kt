package petrova.ola.playlistmaker.player.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import petrova.ola.playlistmaker.utils.msToTime

@Entity(tableName = "track_table")
data class TrackEntity(
    @PrimaryKey
    val id: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String?, //Название альбома
    val releaseDate: String?,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String?,  //Ссылка на трек
){
    val trackTime: String
        get() = msToTime(trackTimeMillis)
}