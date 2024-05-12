package petrova.ola.playlistmaker.media.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.media.playlist.data.db.converters.DbConvertorPlaylist
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistDao
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistEntity
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistRepository
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class PlaylistRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val dbConvertor: DbConvertorPlaylist,
) : PlaylistRepository {
    private val gson = Gson()
    private val playListStorage = ArrayList<Playlist>()

    override suspend fun addTrack(track: Track, playlistId: Long): Boolean {
        var isInsert = false

        getTracks(playlistId)
            .collect { trackList ->
                if (!trackList.contains(track)) {
                    val newList = mutableListOf<Track>()
                    newList.addAll(trackList)
                    newList.add(track)
                    val trackListGson = gson.toJson(newList)
                    playlistDao.updateTracks(playlistId, trackListGson, newList.size)
                    isInsert = true
                }
            }

        return isInsert
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.createPl(dbConvertor.mapToEntity(playlist))
        updatePlaylists()
    }

    override suspend fun updatePlaylists() {
        getPlaylist().collect { result ->
            playListStorage.clear()
            playListStorage.addAll(result)
        }
    }


    override suspend fun deleteTrack(playlistId: Long, track: Track) {
        getTracks(playlistId)
            .collect { tracks ->
                val newList = mutableListOf<Track>()
                newList.addAll(tracks)
                newList.remove(track)
                val trackListGson = Gson().toJson(newList)
                playlistDao.updateTracks(playlistId, trackListGson, newList.size)
            }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePl(dbConvertor.mapToEntity(playlist))
    }

    override fun getTracks(playlistId: Long): Flow<List<Track>> = flow {
        val trackList = playlistDao.getTracks(playlistId)
        emit(trackList.map {
            gson.fromJson(it, Track::class.java)
        })
    }



    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        val playLists= playlistDao.getAll()
        emit(convertFromPlaylistEntity(playLists))
    }


    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> dbConvertor.mapToModel(playlist) }
    }

    private fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
        // Получаем каталог с изображениями внутри специфичного для приложения каталога
        // во внешнем хранилище
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(LOG_TAG, "Директория не создана")
                throw IOException("Directory not created")
            }
        }
        return file
    }

    override suspend fun saveFile(inputFile: String): Uri {
        val uri = Uri.parse(inputFile)

        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath = getAppSpecificAlbumStorageDir(context, "SKIN")

        //создаем каталог, если он не создан
        if (!filePath.exists()) {
            filePath.mkdirs()
        }


        val randomUuid = UUID.randomUUID().toString()
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "$randomUuid.jpg")

        // создаём входящий поток байтов из выбранной картинки
        val inputStream = context.contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        /*val outputStream = */
        return withContext(Dispatchers.IO) {
            val outputStream = FileOutputStream(file)

            // записываем картинку с помощью BitmapFactory
            try {
                BitmapFactory
                    .decodeStream(inputStream)
                    .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            } catch (e: Exception) {
                val drawableImage = context.getDrawable(R.drawable.placeholder)
                if (drawableImage is BitmapDrawable) {
                    val bitmap = drawableImage.bitmap
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
                } else {
                    throw e
                }
            }

            file.toUri()
        }
    }

    override suspend fun deleteFile(uri: Uri) {
        uri.toFile().delete()
    }

    companion object {
        private val LOG_TAG = "LOG_TAG"
    }
}

