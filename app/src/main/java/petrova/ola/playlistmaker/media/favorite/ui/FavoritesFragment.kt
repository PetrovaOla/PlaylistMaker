package petrova.ola.playlistmaker.media.favorite.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentFavoritesBinding
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.EXTRAS_KEY
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.search.ui.SearchRecyclerAdapter
import petrova.ola.playlistmaker.utils.debounce


class FavoritesFragment : Fragment() {

    private val viewModel: FavoritesViewModel by viewModel()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var onClickDebounce: (Track) -> Unit

    private var rvAdapter: SearchRecyclerAdapter? = null
    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        groupNotFound = binding.groupNotFoundMedia
        recycler = binding.trackListFavorites
        groupNotFound.isVisible = false

        viewModel.loadFavoriteTracks()

        rvAdapter = SearchRecyclerAdapter {
            onClickDebounce(it)
        }
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = rvAdapter


        onClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            openPlayer(it)
        }


        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is FavoritesState.Content -> showContent(screenState)
                is FavoritesState.Empty -> showEmpty()
                is FavoritesState.Loading -> showProgress()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadFavoriteTracks()
    }

    private fun showProgress() {
        binding.apply {
            groupNotFoundMedia.isVisible = false
            trackListFavorites.isVisible = false
            progressBarMedia.isVisible = true
        }
    }

    private fun showEmpty() {
        binding.apply {
            groupNotFoundMedia.isVisible = true
            trackListFavorites.isVisible = false
            progressBarMedia.isVisible = false
        }
    }

    private fun showContent(screenState: FavoritesState.Content) {
        binding.apply {
            rvAdapter!!.tracks.clear()
            rvAdapter!!.tracks.addAll(screenState.favoritesList)
            rvAdapter?.notifyDataSetChanged()
            groupNotFoundMedia.isVisible = false
            trackListFavorites.isVisible = true
            progressBarMedia.isVisible = false
        }
    }


    private fun openPlayer(track: Track) {
        val args = Bundle()
        args.putSerializable(EXTRAS_KEY, track)
        findNavController().navigate(
            R.id.action_mediaFragment_to_playerFragment,
            args
        )

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
    }

    companion object {
        fun newInstance() = FavoritesFragment()
        private const val CLICK_DEBOUNCE_DELAY = 200L
    }
}