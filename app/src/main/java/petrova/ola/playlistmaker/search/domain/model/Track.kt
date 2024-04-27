package petrova.ola.playlistmaker.search.domain.model

import android.os.Parcel
import android.os.Parcelable
import petrova.ola.playlistmaker.utils.msToTime

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, //Название альбома
    val releaseDate: String?,//Год релиза трека
    val primaryGenreName: String,//Жанр трека
    val country: String, //Страна исполнителя
    val previewUrl: String?,  //Ссылка на трек
) : Parcelable {
    val bigImg: String
        get() = artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    val date: String
        get() = releaseDate?.substringBefore('-').toString()
    val trackTime: String
        get() = msToTime(trackTimeMillis)

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeInt(trackTimeMillis)
        parcel.writeString(artworkUrl100)
        parcel.writeString(collectionName)
        parcel.writeString(releaseDate)
        parcel.writeString(primaryGenreName)
        parcel.writeString(country)
        parcel.writeString(previewUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}