package petrova.ola.playlistmaker.play.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.ActivityPlayBinding
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchFragment
import petrova.ola.playlistmaker.utils.GsonBundleCodec
import petrova.ola.playlistmaker.utils.ImageLoader

class PlayActivity : AppCompatActivity() {

    private val bundleCodecTrack: GsonBundleCodec<Track> by inject()
    private val imageLoader: ImageLoader by inject()

    private var _binding: ActivityPlayBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PlayViewModel by viewModel()

    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play)
        _binding = ActivityPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intent.extras?.getString(SearchFragment.EXTRAS_KEY)?.let {
            track = bundleCodecTrack.decodeData(it)
        }

        viewModel.setDataSource(url = track.previewUrl.toString())

        binding.toolbarPlayer.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.playButton.setOnClickListener {
            viewModel.onPlayButtonClicked()
        }
        viewModel.observePlayerState().observe(this) {
            binding.playButton.isEnabled = it.isPlayButtonEnabled

            binding.timerTextView.text = it.progress
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }


}