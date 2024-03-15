package petrova.ola.playlistmaker.presentation.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import petrova.ola.playlistmaker.creator.Creator
import petrova.ola.playlistmaker.domain.models.Track

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val tracksInteractor = Creator.provideTracksInteractor(getApplication<Application>())
    private val handler = Handler(Looper.getMainLooper())
    private var searchQuery = SEARCH_EMPTY
    private var latestSearchText: String? = null
    private val historyTrackList = ArrayList<Track>()

    //Screen state
    private val stateLiveData = MutableLiveData<SearchScreenState>(SearchScreenState.Loading)
    val searchScreenState: LiveData<SearchScreenState> = stateLiveData
    fun observeState(): LiveData<SearchScreenState> = mediatorStateLiveData

    init {

    }

    private val mediatorStateLiveData = MediatorLiveData<SearchScreenState>().also { liveData ->
        // 1
        liveData.addSource(stateLiveData) { trackState ->
            liveData.value = when (trackState) {
                is SearchScreenState.Error -> trackState
                is SearchScreenState.Loading -> trackState
                is SearchScreenState.historyTrack -> TODO()
                is SearchScreenState.trackList -> SearchScreenState.trackList(trackState.resultsList.sortedByDescending { it.trackName })
            }
        }
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val SEARCH_EMPTY = ""
        private val token = Any()
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                SearchViewModel(this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application)
            }
        }
    }

}