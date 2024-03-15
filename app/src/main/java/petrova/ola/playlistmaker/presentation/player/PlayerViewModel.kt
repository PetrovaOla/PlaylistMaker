package petrova.ola.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import petrova.ola.playlistmaker.creator.Creator
import petrova.ola.playlistmaker.domain.api.PlayerInteractor

class PlayerViewModel(val playerInteractor: PlayerInteractor) : ViewModel() {

    private val _playerScreenState = MutableLiveData<PlayerScreenState>(PlayerScreenState.Loading)
    val playerScreenState: LiveData<PlayerScreenState> = _playerScreenState

    init {
        _playerScreenState.value = PlayerScreenState.Content()
    }

    private val token = Any()
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }
    private var timer: Runnable = Runnable { timerRun() }

    private var playerState = playerInteractor.playerState()
    private var playerPos = playerInteractor.getPosition()

    private fun timerRun() {
        _playerScreenState.value = PlayerScreenState.Content(playerState, playerPos)
        if (playerState == PlayerState.PLAY) {
            timer.let { mainThreadHandler.postDelayed(it, token, DELAY) }
        }
    }

    private fun play() {
        playerInteractor.start()
        mainThreadHandler.post(timer)
    }

    fun pause() {
        playerInteractor.pause()
        _playerScreenState.value = PlayerScreenState.Content(playerState, playerPos)
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
        mainThreadHandler.removeCallbacksAndMessages(token)
        playerInteractor.destroy()
    }

    companion object {
        private const val DELAY: Long = 500
        fun getViewModelFactory(url: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(
                    playerInteractor = Creator.providePlayerInteractor(url = url)
                )
            }
        }
    }
}