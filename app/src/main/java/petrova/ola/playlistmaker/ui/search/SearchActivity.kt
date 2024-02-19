package petrova.ola.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import petrova.ola.playlistmaker.Creator
import petrova.ola.playlistmaker.data.repository.TracksHistoryRepositoryImplShared
import petrova.ola.playlistmaker.databinding.ActivitySearchBinding
import petrova.ola.playlistmaker.domain.api.TracksInteractor
import petrova.ola.playlistmaker.domain.models.Track
import petrova.ola.playlistmaker.ui.player.PlayerActivity
import kotlin.properties.Delegates


class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private lateinit var inputEditText: TextInputEditText

    private var trackList: MutableList<Track> = mutableListOf()
    private var trackListHistory: MutableList<Track> = mutableListOf()

    private lateinit var tracksInteractor: TracksInteractor

    private var rvAdapter: SearchRecyclerAdapter = SearchRecyclerAdapter(
        SearchRecyclerAdapter.SearchListType.SEARCH, trackList
    ) {
        if (clickDebounce()) {
            appendHistory(it)
            openPlayer(it)
        }
    }
    private var rvAdapterHistory: SearchRecyclerAdapter = SearchRecyclerAdapter(
        SearchRecyclerAdapter.SearchListType.HISTORY,
        trackListHistory
    ) {
        if (clickDebounce()) {
            appendHistory(it)
            openPlayer(it)
        }
    }
    private var currentAdapter: SearchRecyclerAdapter
            by Delegates.observable(rvAdapter) { _, old, new ->
                if (new.type != old.type) recycler.adapter = new
            }


    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group
    private lateinit var groupNotInternet: Group
    private lateinit var updateButton: Button
    private lateinit var historySearchTv: TextView
    private lateinit var clearHistoryButton: Button
    private lateinit var progressBar: ProgressBar

    private var searchQuery = SEARCH_EMPTY
    private val token = Any()


    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable { requestHandler(searchQuery) }

    val tracksHistoryRepository by lazy {
        TracksHistoryRepositoryImplShared(
            getSharedPreferences(APPLICATION_SHARE_ID, MODE_PRIVATE)
        )
    }

    val bundleCodecTrack = Creator.provideBundleCodec<Track>()
    val bundleCodecTrackList = Creator.provideBundleCodec<MutableList<Track>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tracksInteractor = Creator.provideTracksInteractor()

        trackListHistory.addAll(tracksHistoryRepository.getHistory())

        groupNotFound = binding.groupNotFound
        groupNotInternet = binding.groupNotInternet
        groupNotFound.isGone = true
        groupNotInternet.isGone = true
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
            val historyVisible = if (
                hasFocus &&
                inputEditText.text!!.isEmpty() &&
                trackListHistory.isNotEmpty()
            ) View.VISIBLE else View.GONE

            historySearchTv.visibility = historyVisible
            clearHistoryButton.visibility = historyVisible
            currentAdapter =
                if (hasFocus && inputEditText.text!!.isEmpty()) rvAdapterHistory else rvAdapter
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                groupNotFound.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString().orEmpty()

                val historyVisible = if (
                    inputEditText.hasFocus() &&
                    s?.isEmpty() == true &&
                    trackListHistory.isNotEmpty()
                ) View.VISIBLE else View.GONE
                historySearchTv.visibility = historyVisible
                clearHistoryButton.visibility = historyVisible

                if (historyVisible == View.VISIBLE)
                    recycler.visibility = View.VISIBLE

                currentAdapter = if (inputEditText.hasFocus() && inputEditText.text!!.isEmpty())
                    rvAdapterHistory
                else rvAdapter
            }

            override fun afterTextChanged(s: Editable?) {
                searchDebounce()
            }
        }


        inputEditText.addTextChangedListener(textWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // ВЫПОЛНЯЙТЕ ПОИСКОВЫЙ ЗАПРОС ЗДЕСЬ
                requestHandler(searchQuery)
                inputEditText.clearFocus()
            }

            false
        }

        binding.inputLayoutText.setEndIconOnClickListener {
            inputEditText.setText(SEARCH_EMPTY)
            currentAdapter = rvAdapterHistory

            val visibility = if (trackListHistory.isEmpty()) View.GONE else View.VISIBLE
            recycler.visibility = visibility
            historySearchTv.visibility = visibility
            clearHistoryButton.visibility = visibility

            val oldSize = trackList.size
            trackList.clear()
            rvAdapter.notifyItemRangeRemoved(0, oldSize)

            groupNotInternet.isGone = true
            groupNotFound.isGone = true
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            inputEditText.clearFocus()
        }

        recycler.layoutManager = LinearLayoutManager(this)

        currentAdapter = rvAdapterHistory

        updateButton.setOnClickListener {
            requestHandler(searchQuery)
        }

        clearHistoryButton.setOnClickListener {
            with(trackListHistory.size) {
                trackListHistory.clear()
                tracksHistoryRepository.clearHistory()
                rvAdapterHistory.notifyItemRangeRemoved(0, this)
            }
            updateHistoryVisibility()
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacksAndMessages(token)
        handler.postDelayed(searchRunnable, token, SEARCH_DEBOUNCE_DELAY)
    }

    private fun updateHistoryVisibility() {
        with(
            if (
                inputEditText.hasFocus() &&
                inputEditText.text!!.isEmpty() &&
                trackListHistory.isNotEmpty()
            ) View.VISIBLE
            else View.GONE
        ) {
            historySearchTv.visibility = this
            clearHistoryButton.visibility = this
        }
    }

    private fun openPlayer(track: Track) {
        val intent = Intent(this, PlayerActivity::class.java)
        intent.putExtra(EXTRAS_KEY, bundleCodecTrack.encodeData(track))
        startActivity(intent)
    }

    private fun appendHistory(track: Track) {
        when (val indexOf = trackListHistory.indexOf(track)) {
            -1 -> {
                trackListHistory.add(0, track)
                rvAdapterHistory.notifyItemInserted(0)
            }

            0 -> {

            }

            else -> {
                trackListHistory.removeAt(indexOf)
                trackListHistory.add(0, track)
                rvAdapterHistory.notifyItemMoved(indexOf, 0)
            }
        }

        for (i in 10 until trackListHistory.size)
            trackListHistory.removeAt(i)
        rvAdapterHistory.notifyItemRangeRemoved(10, trackListHistory.size - 1)

        updateHistoryVisibility()

        tracksHistoryRepository.putHistory(trackListHistory)
    }

    val providerTracksInteractor = Creator.provideTracksInteractor()
    private fun requestHandler(searchQuery: String) {
        if (searchQuery.isEmpty())
            return

        recycler.isGone = true
        progressBar.isVisible = true

        providerTracksInteractor.searchTracks(
            searchQuery,
            object : TracksInteractor.TracksConsumer {
                override fun consume(trackResponse: List<Track>) {
                    mainExecutor.execute {
                        progressBar.isGone =
                            true   // Прячем ProgressBar после успешного выполнения запроса

                        if (trackResponse.isEmpty()) {
                            groupNotFound.isVisible = true
                            groupNotInternet.isGone = true
                            trackList.clear()
                        } else {
                            recycler.isVisible = true
                            trackList.clear()
                            trackList.addAll(trackResponse)
                            groupNotFound.isGone = true
                            groupNotInternet.isGone = true
                        }
                        rvAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure() {
                    mainExecutor.execute {
                        groupNotInternet.isVisible = true
                        groupNotFound.isGone = true
                        progressBar.isGone = true
                        val size = trackList.size
                        trackList.clear()
                        rvAdapter.notifyItemRangeRemoved(0, size)
                    }
                }
            })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH, searchQuery)
        outState.putInt(NOT_INTERNET, groupNotInternet.visibility)
        outState.putInt(NOT_FOUND, groupNotFound.visibility)
        outState.putString(TRACK_LIST, bundleCodecTrackList.encodeData(trackList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH, SEARCH_EMPTY)
            ?: SEARCH_EMPTY
        inputEditText.setText(searchQuery)

        trackList.addAll(
            bundleCodecTrackList.decodeData(
                savedInstanceState.getString(TRACK_LIST, "[]")
            )
//            gson.fromJson<Array<Track>>(
//                ,savedInstanceState.getString(TRACK_LIST)
//                TypeToken.getArray(Track::class.java).type
//            )
        )
        rvAdapter.notifyDataSetChanged()

        groupNotInternet.visibility = savedInstanceState.getInt(NOT_INTERNET, View.GONE)
        groupNotFound.visibility = savedInstanceState.getInt(NOT_FOUND, View.GONE)
    }

    override fun onStart() {
        super.onStart()
        inputEditText.requestFocus()
    }

    companion object {
        private const val SEARCH = "SEARCH"
        private const val SEARCH_EMPTY = ""
        private const val NOT_INTERNET = "NOT_INTERNET"
        private const val NOT_FOUND = "NOT_FOUND"
        private const val TRACK_LIST = "TRACK_LIST"
        const val SEARCH_HISTORY_SHARED = "search_history_shared"
        const val APPLICATION_SHARE_ID = "application_share_id"
        const val EXTRAS_KEY: String = "TRACK"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

}