package petrova.ola.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.search.domain.api.TracksInteractor
import petrova.ola.playlistmaker.search.domain.model.Track
import petrova.ola.playlistmaker.utils.debounce

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
) : ViewModel() {

    var latestSearchText: String? = null
        private set
//    private var searchJob: Job? = null

    private val historyTrackList: ArrayList<Track> by lazy {
        ArrayList(tracksInteractor.getHistory())
    }
    private val trackList: ArrayList<Track> = ArrayList()

    //Screen state
    private val stateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    private val searchScreenState: LiveData<SearchScreenState> = stateLiveData

    private val mediatorStateLiveData = MediatorLiveData<SearchScreenState>().also { liveData ->
        // 1
        liveData.addSource(stateLiveData) { trackState ->
            liveData.value = when (trackState) {
                is SearchScreenState.Empty -> trackState
                is SearchScreenState.Error -> trackState
                is SearchScreenState.Loading -> trackState
                is SearchScreenState.HistoryTracks -> trackState
                is SearchScreenState.TrackList -> SearchScreenState.TrackList(
                    trackState.resultsList.sortedByDescending { it.trackName }
                )
            }
        }
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_EMPTY = ""
    }

    fun getScreenStateLiveData() = searchScreenState

    private fun renderState(state: SearchScreenState) {
        stateLiveData.postValue(state)
    }

    fun endInput() {
        renderState(SearchScreenState.Empty)
    }

    /*fun searchDebounce(changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        latestSearchText = changedText
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            forceResearch(changedText)
        }

    }*/
    private val trackSearchDebounce = debounce<String>(
        delayMillis = SEARCH_DEBOUNCE_DELAY,
        coroutineScope = viewModelScope,
        useLastParam = true
    ) { changedText ->
        forceResearch(changedText)
    }

    fun searchDebounce(changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun forceResearch(searchQuery: String) {
        if (searchQuery.isEmpty()) {
            renderState(SearchScreenState.HistoryTracks(historyTrackList))
            return
        } else {
            renderState(SearchScreenState.Loading)
            viewModelScope.launch {
                tracksInteractor
                    .searchTracks(searchQuery)
                    .collect { result ->
                        processResult(result.first, result.second)
                    }
            }
        }

    }

    private fun processResult(searchTrack: List<Track>?, errorMessage: String?) {

        if (searchTrack != null) {
            trackList.addAll(searchTrack)
        }

        when {
            errorMessage != null -> {
                renderState(SearchScreenState.Error(message = errorMessage))
            }

            trackList.isEmpty() -> {
                renderState(SearchScreenState.Empty)
            }

            else -> {
                renderState(
                    SearchScreenState.TrackList(
                        resultsList = searchTrack!!
                    )
                )
            }
        }
    }

    fun processHistory(track: Track) {
        when (val indexOf = historyTrackList.indexOf(track)) {
            -1 -> {
                historyTrackList.add(0, track)
            }

            0 -> {

            }

            else -> {
                historyTrackList.removeAt(indexOf)
                historyTrackList.add(0, track)
            }
        }

        for (i in 10 until historyTrackList.size)
            historyTrackList.removeAt(i)

        tracksInteractor.putHistory(historyTrackList)

        if (stateLiveData.value is SearchScreenState.HistoryTracks) {
            stateLiveData.postValue(SearchScreenState.HistoryTracks(historyTrackList))
        }
    }

    fun changeInputFocus(hasFocus: Boolean, empty: Boolean) {
        if (hasFocus) {
            if (empty)
                renderState(SearchScreenState.HistoryTracks(historyTrackList))
            else
                renderState(SearchScreenState.TrackList(trackList))
        } else {
            renderState(SearchScreenState.Empty)
        }
    }

    fun clearHistory() {
        historyTrackList.clear()
        tracksInteractor.clearHistory()
        renderState(SearchScreenState.HistoryTracks(historyTrackList))
    }
}