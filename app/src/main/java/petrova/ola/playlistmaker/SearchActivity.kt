package petrova.ola.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import petrova.ola.playlistmaker.api.ApiService
import petrova.ola.playlistmaker.data.Track
import petrova.ola.playlistmaker.data.TrackResponse
import petrova.ola.playlistmaker.databinding.ActivitySearchBinding
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
    private lateinit var trackList: MutableList<Track>
    private lateinit var recycler: RecyclerView
    private lateinit var groupNotFound: Group
    private lateinit var groupNotInternet: Group
    private lateinit var updateButton: Button
    private val gson = Gson()
    private var searchQuery = SEARCH_EMPTY

    private val baseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        groupNotFound = binding.groupNotFound
        groupNotInternet = binding.groupNotInternet
        groupNotFound.isGone = true
        groupNotInternet.isGone = true
        recycler = binding.trackListRecycler
        inputEditText = binding.inputEditText
        updateButton = binding.updateButton

        binding.toolbarSearch.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery = s?.toString() ?: SEARCH_EMPTY
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
                true
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
        rvAdapter = SearchRecyclerAdapter(trackList)
        recycler.adapter = rvAdapter


        updateButton.setOnClickListener {
            requestHandler(searchQuery)
        }

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

        groupNotInternet.visibility = savedInstanceState.getInt(NOT_INTERNET, View.GONE)
        groupNotFound.visibility = savedInstanceState.getInt(NOT_FOUND, View.GONE)
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, SearchActivity::class.java)

        private const val SEARCH = "SEARCH"
        private const val SEARCH_EMPTY = ""
        private const val NOT_INTERNET = "NOT_INTERNET"
        private const val NOT_FOUND = "NOT_FOUND"
        private const val TRACK_LIST = "TRACK_LIST"

    }

}