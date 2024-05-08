package petrova.ola.playlistmaker.media.ui.new_playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentNewPlayListBinding


class NewPlayListFragment : Fragment() {

    private var _binding: FragmentNewPlayListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NewPlaylistViewModel by viewModel()

    private lateinit var image: ImageView
    private lateinit var createPlaylist: Button
    private lateinit var inputEditTextName: TextInputEditText
    private lateinit var inputEditTextDescription: TextInputEditText
    private lateinit var appBar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlayListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = false

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.setNavigationIcon(R.drawable.arrow_back)
        appBar.setNavigationOnClickListener { findNavController().navigateUp()}


        createPlaylist = binding.createPlaylist
        image = binding.newImgPlaceholder
        inputEditTextName = binding.inputEditTextName
        inputEditTextDescription = binding.inputEditTextDescription


        inputEditTextName.setOnFocusChangeListener { _, hasFocus ->

        }
        inputEditTextDescription.setOnFocusChangeListener { _, hasFocus ->

        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NewPlayListFragment()

    }
}