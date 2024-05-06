package petrova.ola.playlistmaker.player.ui

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityPlayerBinding
import petrova.ola.playlistmaker.player.domain.PlayerState
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchFragment
import petrova.ola.playlistmaker.utils.ImageLoader
import petrova.ola.playlistmaker.utils.msToTime

class PlayerActivity : AppCompatActivity() {
    private val imageLoader: ImageLoader by inject()

    private var isClickAllowed = true

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModel{
        parametersOf(track)
    }

    private lateinit var track: Track

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = when {
            SDK_INT >= Build.VERSION_CODES.TIRAMISU -> intent.getSerializableExtra(
                SearchFragment.EXTRAS_KEY,
                Track::class.java
            ) as Track

            else -> intent.getSerializableExtra(SearchFragment.EXTRAS_KEY) as Track
        }

        viewModel.setDataSource(url = track.previewUrl.toString())

        binding.toolbarPlayer.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.play.setOnClickListener {
            viewModel.onPlayClick()
        }
        viewModel.observeIsFavorite().observe(this) { isFavorite ->
            if (isFavorite == true) {
                binding.buttonLike.setImageResource(R.drawable.button_like)

            } else {
                binding.buttonLike.setImageResource(R.drawable.button_not_like)

            }
        }

        binding.buttonLike.setOnClickListener {
            if (debounceClick()) {
                viewModel.onFavoriteClicked()
            }
        }

        viewModel.playerScreenState.observe(this) { playerScreenState ->
            when (playerScreenState) {
                is PlayerScreenState.Content -> {
                    settingPlayer(
                        playerState = playerScreenState.playerState,
                        position = playerScreenState.playerPos
                    )
                    println()
                }

                is PlayerScreenState.Error -> {
                    Log.d(TAG, "PlayerScreenState.ERROR")
                }

                is PlayerScreenState.Loading -> {
                    Log.d(TAG, "PlayerScreenState.Loading")
                }

                else -> {
                    Log.d(TAG, "Player Else")
                }

            }

        }

        with(binding) {
            imageLoader.loadImage(
                imageUrl = track.bigImg,
                context = this.main,
                placeholder = R.drawable.album,
                errorPlaceholder = R.drawable.album,
                into = poster
            )

            name.text = track.trackName
            artist.text = track.artistName
            duration.text = msToTime(track.trackTimeMillis)
            album.text = track.collectionName ?: ""
            album.isVisible = album.text.isNotEmpty()
            albumTv.isVisible = album.isVisible
            if (track.releaseDate != null && track.releaseDate!!.length > 3) {
                year.text = track.releaseDate!!.substring(0, 4)
            }
            genre.text = track.primaryGenreName
            country.text = track.country
        }

    }

    private fun debounceClick(): Boolean {

        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun settingPlayer(playerState: PlayerState, position: Int) {
        when (playerState) {
            PlayerState.INITIAL -> {
                binding.play.setImageResource(R.drawable.play)
                binding.durationTrack.text = msToTime(0)
            }

            PlayerState.PREPARE -> {
                binding.play.setImageResource(R.drawable.play)
                binding.durationTrack.text = msToTime(0)
            }

            PlayerState.RESUME -> {
                binding.play.setImageResource(R.drawable.play)
                binding.durationTrack.text = msToTime(0)
            }

            PlayerState.PLAY -> {
                binding.play.setImageResource(R.drawable.pause)
                binding.durationTrack.text = msToTime(position)
            }

            PlayerState.PAUSE -> {
                binding.play.setImageResource(R.drawable.play)
                binding.durationTrack.text = msToTime(position)
            }

            PlayerState.ERROR -> {
                Log.d(TAG, "PlayerState.ERROR")
            }

            PlayerState.DESTROY -> {
                Log.d(TAG, "PlayerState.DESTROY")
            }

        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    companion object {
        private const val TAG = "Player Fragment"
        private const val CLICK_DEBOUNCE_DELAY = 200L
    }
}


