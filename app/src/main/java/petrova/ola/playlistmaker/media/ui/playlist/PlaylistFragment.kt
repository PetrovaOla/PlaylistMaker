package petrova.ola.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.Group
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentPlaylistBinding
import petrova.ola.playlistmaker.media.domain.model.Playlist
import petrova.ola.playlistmaker.root.ui.RootActivity
import petrova.ola.playlistmaker.utils.debounce


class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getString(TRACK_ID))
    }
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var rvAdapter: PlaylistAdapter? = null

    private lateinit var createButton: Button
    private lateinit var groupNotFound: Group
    private lateinit var rv: RecyclerView

    private lateinit var onClickDebounce: (Playlist) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupNotFound = binding.groupNotFoundPlaylist
        createButton = binding.createBtn
        rv = binding.playlistRv

        rvAdapter = PlaylistAdapter {
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onClickDebounce(it)
        }
        onClickDebounce = debounce<Playlist>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            openPlaylist(it)
        }
        rv.layoutManager = GridLayoutManager(requireContext(),2,)
        rv.adapter = rvAdapter

        binding.createBtn.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_newPlayListFragment)
        }

    }


    private fun openPlaylist(playlist: Playlist) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        rv.adapter = null
    }

    companion object {
        private const val TRACK_ID = "track_id"
        private const val CLICK_DEBOUNCE_DELAY = 200L

        /* fun newInstance(trackId: String) = PlaylistFragment().apply {
             arguments = Bundle().apply {
                 putString(TRACK_ID, trackId)
             }
         }*/
        fun newInstance() = PlaylistFragment()
    }
}