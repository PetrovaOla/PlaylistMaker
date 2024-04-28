package petrova.ola.playlistmaker.player.ui

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityPlayerBinding
import petrova.ola.playlistmaker.player.domain.PlayerState
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchFragment
import petrova.ola.playlistmaker.utils.ImageLoader
import petrova.ola.playlistmaker.utils.msToTime

class PlayerActivity : AppCompatActivity() {
    private val imageLoader: ImageLoader by inject()


    private var _binding: ActivityPlayerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayerViewModel by viewModel()

    private lateinit var track: Track

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = when {
            SDK_INT >= Build.VERSION_CODES.TIRAMISU -> intent.getSerializableExtra(
                SearchFragment.EXTRAS_KEY,
                Track::class.java
            )!!

            else -> intent.getSerializableExtra(SearchFragment.EXTRAS_KEY) as Track
        }
        viewModel.setDataSource(url = track.previewUrl.toString())

        binding.toolbarPlayer.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.play.setOnClickListener {
            viewModel.onClick()
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

                PlayerScreenState.Error -> {
                    Log.d(TAG, "PlayerScreenState.ERROR")
                }

                PlayerScreenState.Loading -> {
                    Log.d(TAG, "PlayerScreenState.Loading")
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
            album.text = track.collectionName
            album.isVisible = album.text.isNotEmpty()
            albumTv.isVisible = album.isVisible
            if (track.releaseDate != null && track.releaseDate!!.length > 3) {
                year.text = track.releaseDate!!.substring(0, 4)
            }
            genre.text = track.primaryGenreName
            country.text = track.country
        }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val TAG = "Player Fragment"
    }
}


