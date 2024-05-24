package petrova.ola.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import petrova.ola.playlistmaker.R
import petrova.ola.playlistmaker.databinding.FragmentSearchBinding
import petrova.ola.playlistmaker.root.ui.RootActivity
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.CLICK_DEBOUNCE_DELAY
import petrova.ola.playlistmaker.root.ui.RootActivity.Companion.EXTRAS_KEY
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.utils.debounce

@SuppressLint("NotifyDataSetChanged")
class SearchFragment : Fragment() {
    private val viewModel by viewModel<SearchViewModel>()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var inputEditText: TextInputEditText

    private lateinit var onTrackClickDebounce: (Track) -> Unit

    private var rvAdapter: SearchRecyclerAdapter? = null

    private var textWatcher: TextWatcher? = null

    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group
    private lateinit var groupNotInternet: Group
    private lateinit var updateButton: Button
    private lateinit var historySearchTv: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var appBar: Toolbar
    private lateinit var bottomNavView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        groupNotFound = binding.groupNotFound
        groupNotInternet = binding.groupNotInternet
        groupNotFound.visibility = View.GONE
        groupNotInternet.visibility = View.GONE
        recycler = binding.trackListRecycler
        inputEditText = binding.inputEditText
        updateButton = binding.updateButton
        clearHistoryButton = binding.clearHistoryBtn
        historySearchTv = binding.historySearchTv
        progressBar = binding.progressBar

        bottomNavView = requireActivity().findViewById(R.id.bottomNavigationView)
        bottomNavView.isVisible = true

        appBar = requireActivity().findViewById(R.id.toolbar)
        appBar.isVisible = true

        rvAdapter = SearchRecyclerAdapter {
            (activity as RootActivity).animateBottomNavigationView(View.GONE)
            onTrackClickDebounce(it)
            viewModel.processHistory(it)
        }
        onTrackClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) {
            openPlayer(it)
        }


        viewModel.getScreenStateLiveData().observe(viewLifecycleOwner) { screenState ->
            when (screenState) {
                is SearchScreenState.Empty -> showEmpty()
                is SearchScreenState.Error -> showError(screenState)
                is SearchScreenState.Loading -> showLoading()
                is SearchScreenState.HistoryTracks -> showHistoryTracks(screenState)
                is SearchScreenState.TrackList -> showSearchTracks(screenState)
            }

        }

        val originalListener = binding.inputLayoutText.editText?.onFocusChangeListener
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            viewModel.changeInputFocus(hasFocus, inputEditText.text!!.isEmpty())
            originalListener?.onFocusChange(view, hasFocus)
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                groupNotFound.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s?.toString().orEmpty())
            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.searchDebounce(s?.toString().orEmpty())
            }
        }


        inputEditText.addTextChangedListener(textWatcher)

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchDebounce(inputEditText.text.toString())
                inputEditText.clearFocus()
            }

            false
        }

        binding.inputLayoutText.setEndIconOnClickListener {
            inputEditText.setText(EMPTY)

            viewModel.endInput()

            val inputMethodManager = requireContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
        }

        recycler.layoutManager = LinearLayoutManager(requireContext())

        updateButton.setOnClickListener {
            viewModel.latestSearchText?.let { text -> viewModel.forceResearch(text) }
        }
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        recycler.adapter = rvAdapter
    }

    private fun showSearchTracks(screenState: SearchScreenState.TrackList) {
        groupNotInternet.visibility = View.GONE
        groupNotFound.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        historySearchTv.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE

        rvAdapter!!.tracks.clear()
        rvAdapter!!.tracks.addAll(screenState.resultsList)
        rvAdapter!!.notifyDataSetChanged()
    }

    private fun showHistoryTracks(screenState: SearchScreenState.HistoryTracks) {
        groupNotInternet.visibility = View.GONE
        groupNotFound.visibility = View.GONE
        recycler.visibility = View.VISIBLE
        progressBar.visibility = View.GONE

        val visiblity = if (screenState.historyList.isEmpty()) View.GONE
        else View.VISIBLE
        historySearchTv.visibility = visiblity
        clearHistoryButton.visibility = visiblity

        rvAdapter!!.tracks.clear()
        rvAdapter!!.tracks.addAll(screenState.historyList)
        rvAdapter!!.notifyDataSetChanged()
    }

    private fun showLoading() {
        groupNotInternet.visibility = View.GONE
        groupNotFound.visibility = View.GONE
        recycler.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        clearHistoryButton.visibility = View.GONE
        historySearchTv.visibility = View.GONE
    }

    private fun showError(screenState: SearchScreenState.Error) {
        groupNotInternet.visibility = View.VISIBLE
        groupNotFound.visibility = View.GONE
        recycler.visibility = View.GONE
        progressBar.visibility = View.GONE
        binding.errorMessageTv.text = screenState.message
        updateButton.visibility = View.VISIBLE
        clearHistoryButton.visibility = View.GONE
        historySearchTv.visibility = View.GONE
    }

    private fun showEmpty() {
        groupNotInternet.visibility = View.GONE
        groupNotFound.visibility = View.VISIBLE
        recycler.visibility = View.GONE
        progressBar.visibility = View.GONE
        updateButton.visibility = View.GONE
    }

    private fun openPlayer(track: Track) {
        val args = Bundle()
        args.putSerializable(EXTRAS_KEY, track)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment,
            args
        )
    }

    override fun onStart() {
        super.onStart()
        inputEditText.requestFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        rvAdapter = null
        recycler.adapter = null
        textWatcher?.let { inputEditText.removeTextChangedListener(it) }
    }

    companion object {

        private const val EMPTY = ""
    }

}