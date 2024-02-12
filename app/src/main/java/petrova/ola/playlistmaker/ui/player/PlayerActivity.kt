package petrova.ola.playlistmaker.ui.player

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityPlayerBinding
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.ui.search.SearchActivity
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var playerState = STATE_DEFAULT

    private var mediaPlayer = MediaPlayer()
    private lateinit var play: ImageView
    private lateinit var track: Track
    private lateinit var durationTrack: String
    private var currentPosition: String = ""

    private val simpleDateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        play = binding.play
        binding.toolbarPlayer.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        track = Gson().fromJson(
            intent.extras?.getString(SearchActivity.EXTRAS_KEY) ?: "",
            Track::class.java
        )
        val context =
            with(binding) {
                Glide // Отрисовка с Glide
                    .with(this.main)
                    .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .centerCrop()
                    .transform(RoundedCorners(8))
                    .error(R.drawable.album)
                    .placeholder(R.drawable.album)
                    .into(poster)

                name.text = track.trackName
                artist.text = track.artistName
                duration.text = simpleDateFormat.format(track.trackTimeMillis)
                album.text = track.collectionName
                album.isVisible = album.text.isNotEmpty()
                albumTv.isVisible = album.isVisible
                if (track.releaseDate.length > 3) {
                    year.text = track.releaseDate.substring(0, 4)
                }
                genre.text = track.primaryGenreName
                country.text = track.country

            }


        preparePlayer()

        play.setOnClickListener {
            playbackControl()
        }

    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            play.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            play.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.play, theme))
            timer?.let { mainThreadHandler.removeCallbacks(it) }
            binding.durationTrack.text = resources.getString(R.string.time)
        }
    }

    private var timer: Runnable? = null

    private fun startPlayer() {
        mediaPlayer.start()

        playerState = STATE_PLAYING
        play.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.pause, theme))


        timer = object : Runnable {
            override fun run() {
                currentPosition =
                    SimpleDateFormat(
                        "mm:ss",
                        Locale.getDefault()
                    ).format(mediaPlayer.currentPosition)
                binding.durationTrack.text = currentPosition
                mainThreadHandler.postDelayed(this, DELAY)
            }
        }
        timer?.let {
            mainThreadHandler.postDelayed(it, DELAY)
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        play.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.play, theme))

        playerState = STATE_PAUSED
        timer?.let { mainThreadHandler.removeCallbacks(it) }
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY: Long = 500
    }

}


