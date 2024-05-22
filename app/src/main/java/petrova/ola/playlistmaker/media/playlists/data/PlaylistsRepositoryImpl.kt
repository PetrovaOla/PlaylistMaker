package petrova.ola.playlistmaker.media.playlists.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.media.playlists.data.db.converters.DbConvertorPlaylist
import petrova.ola.playlistmaker.media.playlists.data.db.entities.PlaylistDao
import petrova.ola.playlistmaker.media.playlists.domain.db.PlaylistsRepository
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class PlaylistsRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val dbConvertor: DbConvertorPlaylist,
) : PlaylistsRepository {

    override suspend fun addTrack(track: Track, playlist: Playlist): Boolean {
        return if (playlistDao.containsTrack(playlist.id, track.trackId.toLong())) {
            false
        } else {
            playlistDao.addTrack(playlist.id, track.trackId.toLong())
            true
        }
    }

    override suspend fun createPlaylist(playlist: Playlist) {
        playlistDao.createPlaylist(
            dbConvertor.mapToEntity(playlist)
        )
    }

    override suspend fun deleteTrack(playlistId: Long, track: Track) {
        playlistDao.deleteTrack(
            playlistId, track.trackId.toLong()
        )
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistDao.deletePlaylist(dbConvertor.mapToEntity(playlist))
    }

    override fun getTracks(playlistId: Long): Flow<List<Long>> = flow {
        playlistDao.getTracks(playlistId).firstOrNull()?.tracks?.map {
            it.trackId
        }?.let {
            emit(
                it
            )
        }
    }

    override fun getPlaylist(): Flow<List<Playlist>> = flow {
        emit(
            playlistDao
                .getPlaylistsWithTracks()
                .reversed()
                .map(dbConvertor::mapToModel)
        )
    }

    private fun getAppSpecificAlbumStorageDir(context: Context, albumName: String): File {
        // Получаем каталог с изображениями внутри специфичного для приложения каталога
        // во внешнем хранилище
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName)
        if (!file.exists() && !file.mkdirs()){
                Log.e(LOG_TAG, "Директория не создана")
                throw IOException("Directory not created")
            }

        return file
    }
//todo повороты
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

    override suspend fun deleteFile(uri: String?) {
        uri?.toUri()?.toFile()?.delete()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistDao.updatePlaylist(dbConvertor.mapToEntity(playlist))
    }

    companion object {
        private val LOG_TAG = "LOG_TAG"
    }
}

