package petrova.ola.playlistmaker.editplaylist.ui

import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentNewPlaylistBinding
import petrova.ola.playlistmaker.media.playlists.domain.model.Playlist
import petrova.ola.playlistmaker.playlist.ui.PlaylistFragment.Companion.PLAYLIST_KEY
import petrova.ola.playlistmaker.utils.ImageLoader


class EditPlayListFragment : Fragment() {
    private val imageLoader: ImageLoader by inject()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: Playlist

    private lateinit var viewModel: EditPlaylistViewModel

    private lateinit var image: ImageView
    private lateinit var createBtn: Button
    private lateinit var inputName: TextInputEditText
    private lateinit var inputDescription: TextInputEditText
    private lateinit var appBar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView

    private var name: String = EMPTY
    private var description: String = EMPTY
    private var img: String = EMPTY

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = false

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.setNavigationIcon(R.drawable.arrow_back)
        appBar.setNavigationOnClickListener { backPressed() }
        appBar.setTitle(R.string.edit)

        createBtn = binding.createPlaylist
        image = binding.newImgPlaceholder

        inputName = binding.inputEditTextName
        inputDescription = binding.inputEditTextDescription
        createBtn.text = getString(R.string.save)

        playlist = when {
            SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arguments?.getSerializable(
                    PLAYLIST_KEY,
                    Playlist::class.java
                ) as Playlist

            else ->
                arguments?.getSerializable(PLAYLIST_KEY) as Playlist
        }

        viewModel = getViewModel {
            parametersOf(playlist)
        }

        viewModel.getPlaylist()


        imageLoader.loadImage(
            imageUrl = playlist.img ?: "",
            context = requireView(),
            placeholder = R.drawable.album,
            errorPlaceholder = R.drawable.album,
            into = image
        )

        inputName.setText(playlist.name)
        inputDescription.setText(playlist.description ?: EMPTY)


        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    binding.newImgPlaceholder.setImageURI(uri)
                    img = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }
        //по нажатию на кнопку pickImage запускаем photo picker
        image.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        inputName.addTextChangedListener(
            onTextChanged =
            { s: CharSequence?, start: Int, before: Int, count: Int ->
                if (s.isNullOrBlank()) {
                    binding.createPlaylist.isEnabled = false
                } else {
                    name = s.toString()
                    binding.createPlaylist.isEnabled = true

                }
            },
            afterTextChanged =
            { editable ->
                if (editable.toString().isNotEmpty()) {
                    binding.inputLayoutName.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(R.color.YPBlue)
                    )
                }
            }
        )

        inputDescription.addTextChangedListener(
            onTextChanged =
            { s: CharSequence?, start: Int, before: Int, count: Int ->
                if (s.isNullOrBlank()) {
                } else {
                    description = s.toString()
                    binding.inputLayoutDescription.setBoxStrokeColorStateList(
                        requireContext().getColorStateList(
                            R.color.YPBlue
                        )
                    )
                }
            }
        )

        createBtn.setOnClickListener {
            viewModel.updatePlaylist(name = name, description = description, image = img)
            findNavController().navigateUp()

            /* requireActivity().supportFragmentManager.setFragmentResult(
                 PLAYLIST_CREATED,
                 bundleOf(PLAYLIST_NAME to name)
             )*/
        }


    }

    private fun backPressed() {
        if (name.isNotEmpty() || description.isNotEmpty() || img.isNotEmpty()) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.playlist_title))
                .setMessage(getString(R.string.playlist_message))
                .setNegativeButton(getString(R.string.playlist_negative)) { _, _ ->
                }
                .setPositiveButton(getString(R.string.playlist_positive)) { _, _ ->
                    findNavController().navigateUp()
                }
                .show()
        } else findNavController().navigateUp()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val EMPTY = ""
        fun newInstance() = EditPlayListFragment()

    }
}