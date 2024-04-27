package petrova.ola.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import petrova.ola.playlistmaker.player.domain.PlayerInteractor
import petrova.ola.playlistmaker.player.domain.PlayerState

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

    private val playerMutableScreenState = MutableLiveData<PlayerScreenState>(
        PlayerScreenState.Loading
    )
    val playerScreenState: LiveData<PlayerScreenState> = playerMutableScreenState

    init {
        playerMutableScreenState.value = PlayerScreenState.Content()
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

        var timerJob: Job? = null
        timerJob = viewModelScope.launch {
            playerMutableScreenState.value = PlayerScreenState.Content(playerState, playerPos)

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

    fun onClick() {
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

    companion object {
        private const val DELAY: Long = 500
    }
}