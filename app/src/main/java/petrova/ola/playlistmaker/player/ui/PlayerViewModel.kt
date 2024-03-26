package petrova.ola.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    private val token = Any()
    private val mainThreadHandler by lazy { Handler(Looper.getMainLooper()) }
    private var timer: Runnable = Runnable { timerRun() }

    private val playerState
        get() = playerInteractor.playerState()

    private val playerPos
        get() = playerInteractor.getPosition()

    fun setDataSource(url: String) {
        playerInteractor.setDataSource(url)
    }

    private fun timerRun() {
        playerMutableScreenState.value = PlayerScreenState.Content(playerState, playerPos)
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
        playerMutableScreenState.value = PlayerScreenState.Content(playerState, playerPos)
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
    }
}