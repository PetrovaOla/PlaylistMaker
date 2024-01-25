package petrova.ola.playlistmaker.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.databinding.ActivitySearchBinding
import petrova.ola.playlistmaker.model.Track
import petrova.ola.playlistmaker.model.TrackResponse
import petrova.ola.playlistmaker.model.api.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private lateinit var inputEditText: TextInputEditText
    private lateinit var rvAdapter: SearchRecyclerAdapter
    private lateinit var rvAdapterHistory: SearchRecyclerAdapter
    private lateinit var trackList: MutableList<Track>
    private var trackListHistory: MutableList<Track> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group
    private lateinit var groupNotInternet: Group
    private lateinit var updateButton: Button
    private lateinit var historySearchTv: TextView
    private lateinit var clearHistoryButton: Button
    private val gson = Gson()
    private var searchQuery = SEARCH_EMPTY

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun callSave() {
        val sharedPreferences = getSharedPreferences(APPLICATION_SHARE_ID, MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_SHARED, gson.toJson(trackListHistory))
            .apply()
    }

    private val apiService = retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val sharedPreferences = getSharedPreferences(APPLICATION_SHARE_ID, MODE_PRIVATE)

        trackListHistory.addAll(
            gson.fromJson(
                sharedPreferences.getString(SEARCH_HISTORY_SHARED, "[]"),
                Array<Track>::class.java
            )
        )

        groupNotFound = binding.groupNotFound
        groupNotInternet = binding.groupNotInternet
        groupNotFound.isGone = true
        groupNotInternet.isGone = true
        recycler = binding.trackListRecycler
        inputEditText = binding.inputEditText
        updateButton = binding.updateButton
        clearHistoryButton = binding.clearHistoryBtn
        historySearchTv = binding.historySearchTv

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
            recycler.adapter =
                if (hasFocus && inputEditText.text!!.isEmpty()) rvAdapterHistory else rvAdapter
        }
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: SEARCH_EMPTY
                val historyVisible = if (
                    inputEditText.hasFocus() &&
                    s?.isEmpty() == true &&
                    trackListHistory.isNotEmpty()
                ) View.VISIBLE else View.GONE
                historySearchTv.visibility = historyVisible
                clearHistoryButton.visibility = historyVisible
                recycler.adapter =
                    if (inputEditText.hasFocus() && inputEditText.text!!.isEmpty()) rvAdapterHistory else rvAdapter
            }

            override fun afterTextChanged(s: Editable?) {

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
            trackList.clear()
            rvAdapter.notifyDataSetChanged()
            groupNotInternet.isGone = true
            groupNotFound.isGone = true
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(it.windowToken, 0)
            inputEditText.clearFocus()
        }

        trackList = mutableListOf()
        recycler.layoutManager = LinearLayoutManager(this)

        rvAdapter = SearchRecyclerAdapter(trackList) {
            appendHistory(it)
            openPlayer(it)
        }

        rvAdapterHistory = SearchRecyclerAdapter(trackListHistory) {
            appendHistory(it)
            openPlayer(it)
        }
        recycler.adapter = rvAdapterHistory

        updateButton.setOnClickListener {
            requestHandler(searchQuery)
        }

        clearHistoryButton.setOnClickListener {
            with(trackListHistory.size) {
                trackListHistory.clear()
                rvAdapterHistory.notifyItemRangeRemoved(0, this)
            }
            callSave()
            updateHistoryVisibility()
        }
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
        intent.putExtra(EXTRAS_KEY, gson.toJson(track))
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
        callSave()
    }

    private fun requestHandler(searchQuery: String) {
        apiService.getTrack(searchQuery)
            .enqueue(object : Callback<TrackResponse> {

                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    val tracksResponse = response.body()?.results


                    if (response.isSuccessful && tracksResponse != null) {
                        if (tracksResponse.isEmpty()) {
                            groupNotFound.isVisible = true
                            groupNotInternet.isGone = true
                            trackList.clear()
                        } else {
                            trackList.clear()
                            trackList.addAll(tracksResponse.toMutableList())
                            groupNotFound.isGone = true
                            groupNotInternet.isGone = true
                        }
                    } else {
                        trackList.clear()
                        groupNotInternet.isVisible = true
                        groupNotFound.isGone = true
                    }
                    rvAdapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    groupNotInternet.isVisible = true
                    groupNotFound.isGone = true
                    trackList.clear()
                    rvAdapter.notifyDataSetChanged()


                }
            })


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH, searchQuery)
        outState.putInt(NOT_INTERNET, groupNotInternet.visibility)
        outState.putInt(NOT_FOUND, groupNotFound.visibility)
        outState.putString(TRACK_LIST, gson.toJson(trackList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchQuery = savedInstanceState.getString(SEARCH, SEARCH_EMPTY)
            ?: SEARCH_EMPTY
        inputEditText.setText(searchQuery)

        trackList.addAll(
            gson.fromJson<Array<Track>>(
                savedInstanceState.getString(TRACK_LIST),
                TypeToken.getArray(Track::class.java).type
            )
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
        fun newIntent(context: Context) = Intent(context, SearchActivity::class.java)

        private const val SEARCH = "SEARCH"
        private const val SEARCH_EMPTY = ""
        private const val NOT_INTERNET = "NOT_INTERNET"
        private const val NOT_FOUND = "NOT_FOUND"
        private const val TRACK_LIST = "TRACK_LIST"
        private const val SEARCH_HISTORY_SHARED = "search_history_shared"
        const val APPLICATION_SHARE_ID = "application_share_id"
        const val EXTRAS_KEY: String = "TRACK"
    }

}