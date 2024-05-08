package petrova.ola.playlistmaker.media.playlist.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import androidx.core.net.toFile
import androidx.core.net.toUri
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.media.playlist.data.db.converters.DbConvertorPlaylist
import petrova.ola.playlistmaker.media.playlist.data.db.entities.PlaylistDao
import petrova.ola.playlistmaker.media.playlist.domain.db.PlaylistRepository
import petrova.ola.playlistmaker.media.playlist.domain.model.Playlist
import petrova.ola.playlistmaker.search.domain.model.Track
import java.io.File
import java.io.FileOutputStream

class PlaylistRepositoryImpl(
    private val context: Context,
    private val playlistDao: PlaylistDao,
    private val dbConvertor: DbConvertorPlaylist,
) : PlaylistRepository {
    val gson = Gson()


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
        playlist.id = playlistDao.createPl(dbConvertor.mapToEntity(playlist))
        playlist.img = playlist.id.toString() + ".jpg"
        saveFile(playlist.img!!, playlist.id)

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
        val playLists = playlistDao.getAll()
        val result = playLists.map {
            dbConvertor.mapToModel(it)
        }
        emit(result)
    }.flowOn(Dispatchers.IO)

    override suspend fun saveFile(uriStr: String, playlistId: Long): Uri {
        val uri = Uri.parse(uriStr)
        val inputStream = context.contentResolver.openInputStream(uri)

        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "SKIN"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "$playlistId.jpg")
        val outputStream = withContext(Dispatchers.IO) {
            FileOutputStream(file)
        }

        try {
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)


        } catch (e: Exception) {
            val drawableImage = context.getDrawable(R.drawable.placeholder)
            if (drawableImage is BitmapDrawable) {
                val bitmap = drawableImage.bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            }

        }
        return file.toUri()
    }

    override suspend fun loadFile() {
        println()
    }

    override suspend fun deleteFile(uri: Uri) {
        uri.toFile().delete()
    }
}

