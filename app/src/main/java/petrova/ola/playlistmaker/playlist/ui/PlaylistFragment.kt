package petrova.ola.playlistmaker.playlist.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentPlaylistBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.CLICK_DEBOUNCE_DELAY
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.EXTRAS_KEY
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.utils.ImageLoader
import petrova.ola.playlistmaker.utils.debounce
import kotlin.math.roundToInt


class PlaylistFragment : Fragment() {
    private val imageLoader: ImageLoader by inject()
    private lateinit var playlist: Playlist
    private lateinit var viewModel: PlaylistViewModel

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    private lateinit var onClickDebounce: (Track) -> Unit
    private lateinit var onLongClick: (Track) -> Unit

    private var rvAdapter: PlaylistAdapter? = null
    private lateinit var recycler: RecyclerView

    private lateinit var appBar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView

    private var moreBottomSheetBehavior = BottomSheetBehavior<LinearLayout>()
    private var playlistBottomSheetBehavior = BottomSheetBehavior<LinearLayout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.isVisible = false

        binding.backPl.setOnClickListener {
            findNavController().navigateUp()
        }

        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = false

        playlist = when {
            SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getSerializable(
                    EXTRAS_KEY,
                    Playlist::class.java
                ) as Playlist

            else ->
                arguments?.getSerializable(EXTRAS_KEY) as Playlist
        }

        viewModel = getViewModel {
            parametersOf(playlist)
        }
        recycler = binding.rvPl

        playlistBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheetPl)
        moreBottomSheetBehavior = BottomSheetBehavior.from(binding.bottomsheetMore)

        moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        moreBottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.isVisible = true
                    }

                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.isVisible = false
                    }

                    else -> {
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


//            удалить трек
        onLongClick = { track ->
            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle(getString(R.string.delete_track_title))
                .setMessage(getString(R.string.delete_track))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    viewModel.deleteTrack(track)
                    viewModel.trackTime()
                }
                .show()
        }

        onClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            openPlayer(it)
        }

        rvAdapter = PlaylistAdapter(
            mutableListOf(),
            trackOnLongClickListener = onLongClick,
            trackOnClickListener = onClickDebounce
        )

        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = rvAdapter

        with(binding) {
            playlist.img?.let {
                imageLoader.loadImage(
                    imageUrl = it,
                    context = this.root,
                    placeholder = R.drawable.album,
                    errorPlaceholder = R.drawable.album,
                    into = placeholderPl
                )
            }

            namePl.text = playlist.name
            descriptionPl.text = playlist.description
            viewModel.timeLiveData.observe(viewLifecycleOwner) { time ->
                val seconds = (time.toDouble() / 1000).roundToInt()
                durationPl.text = binding.root.resources.getQuantityString(
                    R.plurals.minut_plurals,
                    seconds / 60,
                    seconds / 60
                )
//                "${seconds / 60} "
            }
            durationPl.text = resources.getText(R.string.loading)

            viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
                val newCount = when (screenState) {
                    is PlaylistState.Content -> {
                        rvAdapter?.let {
                            it.tracks.clear()
                            it.tracks.addAll(screenState.tracks)
                            it.notifyDataSetChanged()
                        }
                        showContent(screenState)
                        screenState.tracks.size
                    }

                    is PlaylistState.Empty -> {
                        showEmpty()
                        0
                    }
                }
                countPl.text = binding.root.resources.getQuantityString(
                    R.plurals.track_plurals,
                    newCount,
                    newCount
                )
            }

        }
        viewModel.trackTime()

        binding.morePl.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            updateBottomSheet()
        }

        binding.sharePl.setOnClickListener {
            sharePlaylist(it)
        }
        binding.sharePlBs.setOnClickListener {
            sharePlaylist(it)
        }
        binding.editPlBs.setOnClickListener {
            val args = Bundle()
            args.putSerializable(PLAYLIST_KEY, playlist)
            findNavController().navigate(
                R.id.action_playlistFragment_to_editPlayListFragment,
                args
            )
        }

        binding.deletePlBs.setOnClickListener {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

//            playlistBottomSheetBehavior.isHideable = true
//            playlistBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

            MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)
                .setTitle(getString(R.string.delete_playlist))
                .setMessage(getString(R.string.delete_playlist_title))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    viewModel.deletePlaylist(playlist)
                    findNavController().navigateUp()
                }
                .show()
        }

        viewModel.playlistReload.observe(viewLifecycleOwner) {
            playlist = it
            updateBottomSheet()
            updateData()
        }
    }

    private fun sharePlaylist(view: View) {

        if (playlist.trackIds.size == 0) {
            Snackbar.make(
                view, getString(R.string.no_tracks), Snackbar.LENGTH_LONG
            ).show()
        } else {
            viewModel.sharePlaylist()
        }

        if (view === binding.sharePlBs) {
            moreBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun updateBottomSheet() {
        playlist.img?.let {
            imageLoader.loadImage(
                imageUrl = it,
                context = binding.root,
                placeholder = R.drawable.album,
                errorPlaceholder = R.drawable.album,
                into = binding.imageTrack
            )
        }
        binding.playlistName.text = playlist.name
        binding.playlistCount.text = resources.getQuantityString(
            R.plurals.track_plurals,
            playlist.trackIds.size,
            playlist.trackIds.size
        )
    }

    private fun updateData() {
        playlist.img?.let {
            imageLoader.loadImage(
                imageUrl = it,
                context = binding.root,
                placeholder = R.drawable.album,
                errorPlaceholder = R.drawable.album,
                into = binding.placeholderPl
            )
        }
        binding.namePl.text = playlist.name
        binding.descriptionPl.text = playlist.description
        binding.countPl.text = resources.getQuantityString(
            R.plurals.track_plurals,
            playlist.trackIds.size,
            playlist.trackIds.size
        )
    }


    private fun openPlayer(track: Track) {
        val args = Bundle()
        args.putSerializable(EXTRAS_KEY, track)
        findNavController().navigate(
            R.id.action_playlistFragment_to_playerFragment,
            args
        )
    }

    private fun showEmpty() {
        recycler.isVisible = false
        binding.noTrack.isVisible = true
    }

    private fun showContent(screenState: PlaylistState.Content) {

        rvAdapter!!.tracks.clear()
        rvAdapter!!.tracks.addAll(screenState.tracks)
        rvAdapter!!.notifyDataSetChanged()
        recycler.isVisible = true

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTracks()
        bottomNavView.isVisible = false
        viewModel.requestPlaylistUpdate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()
        const val PLAYLIST_KEY = "Playlist"
    }

}