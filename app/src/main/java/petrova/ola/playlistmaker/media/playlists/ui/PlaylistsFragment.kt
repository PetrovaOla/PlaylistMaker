package petrova.ola.playlistmaker.media.playlists.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentPlaylistsBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.CLICK_DEBOUNCE_DELAY
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.EXTRAS_KEY
import petrova.ola.playlistmaker.utils.debounce


class PlaylistsFragment : Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()
    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private var rvAdapter: PlaylistsAdapter? = null

    private lateinit var createButton: Button
    private lateinit var groupNotFound: LinearLayout
    private lateinit var recycler: RecyclerView


    private lateinit var onClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        requireActivity().supportFragmentManager.setFragmentResultListener(
            PLAYLIST_CREATED, viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(PLAYLIST_NAME)?.let { showToast(it) }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupNotFound = binding.groupNotFoundPlaylist
        createButton = binding.createBtn
        recycler = binding.playlistRv
        groupNotFound.visibility = View.GONE

        viewModel.loadPlaylist()

        rvAdapter = PlaylistsAdapter {
            onClickDebounce(it)
        }

        recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        recycler.adapter = rvAdapter

        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is PlaylistsState.Content -> showContent(screenState)
                is PlaylistsState.Empty -> showEmpty()
            }
        }


        onClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            openPlaylist(it)
        }


        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_newPlayListFragment)
        }

    }

    private fun showEmpty() {
        groupNotFound.visibility = View.VISIBLE
        recycler.visibility = View.GONE
    }

    private fun showContent(screenState: PlaylistsState.Content) {
        rvAdapter?.playlists?.clear()
        rvAdapter!!.playlists.addAll(screenState.playlists)
        rvAdapter!!.notifyDataSetChanged()
        groupNotFound.visibility = View.GONE
        recycler.visibility = View.VISIBLE
    }


    private fun openPlaylist(playlist: Playlist) {
       val args = Bundle()
        args.putSerializable(EXTRAS_KEY, playlist)
        findNavController().navigate(
            R.id.action_mediaFragment_to_playlistFragment,
            args)


    }

    private fun showToast(playlistName: String) {
        val snackbar = Snackbar.make(
            requireActivity().findViewById(R.id.media_root),
            getString(R.string.playlist_show, playlistName),
            Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylist()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
    }

    companion object {
        const val PLAYLIST_CREATED = "playlist_created"
        const val PLAYLIST_NAME = "playlist_name"
        fun newInstance() = PlaylistsFragment()
    }
}