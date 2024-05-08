package petrova.ola.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.player.domain.PlayerInteractor
import petrova.ola.playlistmaker.player.domain.PlayerState
import petrova.ola.playlistmaker.media.favorite.domain.db.FavouritesInteractor
import petrova.ola.playlistmaker.search.domain.model.Track

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favouritesInteractor: FavouritesInteractor,
) : ViewModel() {

   private val playerMutableScreenState = MutableLiveData<PlayerScreenState>(
        PlayerScreenState.Loading
    )
    val playerScreenState: LiveData<PlayerScreenState> = playerMutableScreenState

    private val favoriteLiveData by lazy { MutableLiveData(track.isFavorite) }
    fun observeIsFavorite(): LiveData<Boolean> = favoriteLiveData

    init {
        playerMutableScreenState.value = PlayerScreenState.Content()
        validateIsFavorite()
    }

    private var timerJob: Job? = null

    private val playerState
        get() = playerInteractor.playerState()

    private val playerPos
        get() = playerInteractor.getPosition()


    fun setDataSource(url: String) {
        playerInteractor.setDataSource(url)
    }

    private fun timerRun() {
        timerJob = viewModelScope.launch {

            while ((playerState == PlayerState.PLAY)) {
                playerMutableScreenState.postValue(
                    PlayerScreenState.Content(
                        playerState,
                        playerPos
                    )
                )
                delay(DELAY)
            }
            timerJob?.cancel()
            playerMutableScreenState.postValue(PlayerScreenState.Content(playerState, playerPos))
        }

    }

    private fun play() {
        playerInteractor.start()
        timerRun()
    }

    fun pause() {
        playerInteractor.pause()
        playerMutableScreenState.value = PlayerScreenState.Content(playerState, playerPos)
        timerJob?.cancel()
    }

    fun onPlayClick() {
        if (playerInteractor.playerState() == PlayerState.PLAY) {
            pause()
        } else {
            play()
        }
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.destroy()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                launch(Dispatchers.IO) {
                    favouritesInteractor.deleteFavoritesTrack(track)
                }
                track.isFavorite = false
            } else {
                favouritesInteractor.insertFavoritesTrack(track)
                track.isFavorite = true
            }
            favoriteLiveData.postValue(track.isFavorite)
        }
    }

    private fun validateIsFavorite() {
        viewModelScope.launch {
            favouritesInteractor.getFavoritesTrack().collect { favoritesTracks ->
                favoritesTracks.forEach { favoriteTrack ->
                    if (track.trackId == favoriteTrack.trackId) {
                        track.isFavorite = true
                        track.trackId = favoriteTrack.trackId
                    }
                }
            }
            favoriteLiveData.postValue(track.isFavorite)
        }
    }

    companion object {
        private const val DELAY: Long = 500
    }
}