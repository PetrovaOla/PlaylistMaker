package petrova.ola.playlistmaker.player.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.creator.Creator
import petrova.ola.playlistmaker.databinding.ActivityPlayerBinding
import petrova.ola.playlistmaker.player.domain.PlayerState
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchActivity
import petrova.ola.playlistmaker.utils.msToTime

class PlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.extras?.getString(SearchActivity.EXTRAS_KEY)?.let {
            track = Creator.bundleCodecTrack.decodeData(it)
        }
        viewModel = ViewModelProvider(
            this, PlayerViewModel.getViewModelFactory(track.previewUrl)
        )[PlayerViewModel::class.java]

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
            val imageLoader = Creator.provideImageLoader()
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

    companion object {
        private const val TAG = "Player Activity"
    }
}


