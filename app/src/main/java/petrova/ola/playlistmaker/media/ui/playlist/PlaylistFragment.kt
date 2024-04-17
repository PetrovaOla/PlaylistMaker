package petrova.ola.playlistmaker.media.ui.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.databinding.FragmentPlaylistBinding


class PlaylistFragment : Fragment() {

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf(requireArguments().getString(TRACK_ID))
    }
    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TRACK_ID = "track_id"

        /* fun newInstance(trackId: String) = PlaylistFragment().apply {
             arguments = Bundle().apply {
                 putString(TRACK_ID, trackId)
             }
         }*/
        fun newInstance() = PlaylistFragment()
    }
}