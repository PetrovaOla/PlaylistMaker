package petrova.ola.playlistmaker.playlist.ui

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import petrova.ola.playlistmaker.utils.msToTime


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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.setNavigationIcon(R.drawable.arrow_back)
        appBar.setBackgroundColor(resources.getColor(R.color.YLightGray))
        appBar.setNavigationOnClickListener { findNavController().navigateUp() }

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

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistState.Content -> {
                    rvAdapter?.let {
                        it.tracks.clear()
                        it.tracks.addAll(screenState.tracks)
                        it.notifyDataSetChanged()
                    }
                    showContent(screenState)
                }

                is PlaylistState.Empty -> showEmpty()
            }
        }

        val bottomSheetContainer = binding.bottomsheetPl
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }

                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })


        //bottomsheet поделиться
        val bottomSheetContainerMore = binding.bottomsheetMore
        val bottomSheetBehaviorMore = BottomSheetBehavior.from(bottomSheetContainerMore).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        bottomSheetBehaviorMore.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {

                    }

                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

//            удалить трек
        onLongClick = { track ->

            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.delete_track_title))
                .setMessage(getString(R.string.delete_track))
                .setNegativeButton(getString(R.string.no)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.ok)) { _, _ ->
                    viewModel.deleteTrack(track)
                    viewModel.trackTime()
                    viewModel.trackCount()

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


// TODO: при удалении не обновляются время и кол-во треков

        with(binding) {
            imageLoader.loadImage(
                imageUrl = playlist.bigImg,
                context = this.root,
                placeholder = R.drawable.album,
                errorPlaceholder = R.drawable.album,
                into = placeholderPl
            )

            namePl.text = playlist.name
            descriptionPl.text = playlist.description

            viewModel.timeLiveData.observe(viewLifecycleOwner) { time ->
                durationPl.text = msToTime(time)
            }
            durationPl.text = resources.getText(R.string.loading)

            viewModel.countLiveData.observe(viewLifecycleOwner) { count ->
                countPl.text = binding.root.resources.getQuantityString(
                    R.plurals.track_plurals,
                    count,
                    count
                )
            }

        }
        viewModel.trackCount()
        viewModel.trackTime()
        binding.sharePl.setOnClickListener {
        }
        binding.morePl.setOnClickListener {
        }
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
        recycler.visibility = View.GONE
    }

    private fun showContent(screenState: PlaylistState.Content) {

        rvAdapter!!.tracks.clear()
        rvAdapter!!.tracks.addAll(screenState.tracks)
        rvAdapter!!.notifyDataSetChanged()
        recycler.visibility = View.VISIBLE

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
    }

    companion object {
        fun newInstance() = PlaylistFragment()

    }

}