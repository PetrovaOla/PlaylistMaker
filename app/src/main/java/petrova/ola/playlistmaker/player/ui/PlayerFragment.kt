package petrova.ola.playlistmaker.player.ui

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentPlayerBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.media.playlists.ui.PlaylistsFragment.Companion.PLAYLIST_CREATED
import petrova.ola.playlistmaker.media.playlists.ui.PlaylistsFragment.Companion.PLAYLIST_NAME
import petrova.ola.playlistmaker.media.playlists.ui.PlaylistsState
import petrova.ola.playlistmaker.player.domain.PlayerState
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.CLICK_DEBOUNCE_DELAY
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.EXTRAS_KEY
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchFragment
import petrova.ola.playlistmaker.utils.ImageLoader
import petrova.ola.playlistmaker.utils.msToTime

class PlayerFragment : Fragment() {
    private val imageLoader: ImageLoader by inject()

    private var isClickAllowed = true


    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!


    private lateinit var viewModel: PlayerViewModel
    private lateinit var track: Track

    private lateinit var appBar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView

    private var rvAdapter: BottomSheetAdapter? = null
    private lateinit var recycler: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            PLAYLIST_CREATED, viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(PLAYLIST_NAME)?.let { showToast(it) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.setNavigationIcon(R.drawable.arrow_back)
        appBar.setNavigationOnClickListener { findNavController().navigateUp() }

        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = false

        recycler = binding.rvPlaylist

        track = when {
            SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getSerializable(
                    EXTRAS_KEY,
                    Track::class.java
                ) as Track

            else ->
                arguments?.getSerializable(EXTRAS_KEY) as Track
        }


        viewModel = getViewModel {
            parametersOf(track)
        }


        viewModel.setDataSource(url = track.previewUrl.toString())

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { renderPlaylist(it) }

        val bottomSheetContainer = binding.playlistsBottomSheet
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            viewModel.loadPlaylists()
        }

        viewModel.getResultLiveData().observe(viewLifecycleOwner) { (playlistName, isSuccess) ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            val view: CoordinatorLayout? = activity?.findViewById(R.id.main)

            Snackbar.make(
                view!!,
                getString(
                    when(isSuccess) {
                        true -> R.string.add_track_to_playlist
                        false -> R.string.track_added_playlist
                    },
                    playlistName
                ),
                Snackbar.LENGTH_SHORT
            ).show()
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                        appBar.setBackgroundColor(resources.getColor(R.color.bottom_navigation_background))
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                        appBar.setBackgroundColor(resources.getColor(R.color.overlay))
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
        binding.newPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_playerFragment_to_newPlayListFragment)
        }


        rvAdapter = BottomSheetAdapter { it ->
//            if (debounceClick()) {
            viewModel.addTrackPlaylist(playlist = it, track = track)
//            }
        }
        recycler.adapter = rvAdapter
        recycler.layoutManager = LinearLayoutManager(requireContext())



        binding.play.setOnClickListener {
            viewModel.onPlayClick()
        }
        viewModel.observeIsFavorite().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
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
        viewModel.playerScreenState.observe(viewLifecycleOwner) { playerScreenState ->
            renderPlayer(playerScreenState)

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

    private fun renderPlayer(playerScreenState: PlayerScreenState?) {
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

    private fun renderPlaylist(state: PlaylistsState) {
        when (state) {
            is PlaylistsState.Empty -> showEmpty()
            is PlaylistsState.Content -> showContent(state.playlists)
        }
    }

    private fun showContent(playlist: List<Playlist>) {
        rvAdapter?.apply {
            playlists.clear()
            playlists.addAll(playlist)
            notifyDataSetChanged()
        }
        binding.rvPlaylist.visibility = View.VISIBLE
    }

    private fun showEmpty() {
        binding.rvPlaylist.visibility = View.VISIBLE
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

    private fun showToast(playlistName: String) {
        val view: CoordinatorLayout? = activity?.findViewById(R.id.main)
        val snackbar = Snackbar.make(
            view!!,
            getString(R.string.playlist_show, playlistName),
            Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
    }

    companion object {
        private const val TAG = "Player Fragment"
    }
}


