package petrova.ola.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import petrova.ola.playlistmaker.creator.Creator
import petrova.ola.playlistmaker.databinding.ActivitySearchBinding
import petrova.ola.playlistmaker.player.ui.PlayerActivity
import petrova.ola.playlistmaker.search.domain.model.Track

@SuppressLint("NotifyDataSetChanged")
class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> { SearchViewModel.getViewModelFactory() }

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private lateinit var inputEditText: TextInputEditText

    private var rvAdapter = SearchRecyclerAdapter {
        viewModel.processHistory(it)
        openPlayer(it)
    }

    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group
    private lateinit var groupNotInternet: Group
    private lateinit var updateButton: Button
    private lateinit var historySearchTv: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is SearchScreenState.Empty -> {
                    groupNotInternet.visibility = View.GONE
                    groupNotFound.visibility = View.GONE
                    recycler.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    updateButton.visibility = View.GONE
                }

                is SearchScreenState.Error -> {
                    groupNotInternet.visibility = View.VISIBLE
                    groupNotFound.visibility = View.GONE
                    recycler.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    binding.errorMessageTv.text = screenState.message
                    updateButton.visibility = View.VISIBLE
                }

                SearchScreenState.Loading -> {
                    groupNotInternet.visibility = View.GONE
                    groupNotFound.visibility = View.GONE
                    recycler.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    updateButton.visibility = View.GONE
                    clearHistoryButton.visibility = View.GONE
                    historySearchTv.visibility = View.GONE
                }

                is SearchScreenState.HistoryTracks -> {
                    groupNotInternet.visibility = View.GONE
                    groupNotFound.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE

                    val visiblity = if (screenState.historyList.isEmpty()) View.GONE
                    else View.VISIBLE
                    historySearchTv.visibility = visiblity
                    clearHistoryButton.visibility = visiblity

                    rvAdapter.tracks.clear()
                    rvAdapter.tracks.addAll(screenState.historyList)
                    rvAdapter.notifyDataSetChanged()
                }

                is SearchScreenState.TrackList -> {
                    groupNotInternet.visibility = View.GONE
                    groupNotFound.visibility = View.GONE
                    recycler.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    historySearchTv.visibility = View.GONE
                    clearHistoryButton.visibility = View.GONE

                    rvAdapter.tracks.clear()
                    rvAdapter.tracks.addAll(screenState.resultsList)
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }

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
        binding.toolbarSearch.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        inputEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.changeInputFocus(hasFocus, inputEditText.text!!.isEmpty())
        }
        val textWatcher = object : TextWatcher {
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
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                viewModel.searchDebounce(inputEditText.toString())
                inputEditText.clearFocus()
            }

            false
        }

        binding.inputLayoutText.setEndIconOnClickListener {
            inputEditText.setText("")

            viewModel.endInput()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            inputEditText.clearFocus()
        }

        recycler.layoutManager = LinearLayoutManager(this)

        updateButton.setOnClickListener {
            viewModel.forceResearch(viewModel.latestSearchText)
        }
        clearHistoryButton.setOnClickListener {
            viewModel.clearHistory()
        }

        recycler.adapter = rvAdapter
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRAS_KEY, Creator.bundleCodecTrack.encodeData(track))
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        inputEditText.requestFocus()
    }

    companion object {
        const val EXTRAS_KEY: String = "TRACK"
    }

}