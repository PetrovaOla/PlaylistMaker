package petrova.ola.playlistmaker.player.domain

import android.media.MediaPlayer
import android.util.Log

class PlayerInteractorImpl(url: String) : PlayerInteractor {
    private val mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.INITIAL

    init {
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnCompletionListener { playerState = PlayerState.PREPARE }
    }

    override fun playerState(): PlayerState {
        return playerState
    }

    override fun start() {
        if (playerState != PlayerState.ERROR) {
            if (playerState == PlayerState.INITIAL) {
                try {
                    mediaPlayer.prepare()
                    playerState = PlayerState.PREPARE
                } catch (e: Exception) {
                    playerState = PlayerState.ERROR
                } finally {
                    if (playerState == PlayerState.ERROR) {
                        mediaPlayer.stop()
                    } else {
                        playerState = PlayerState.RESUME
                    }
                    Log.d("PlayerInteractorImpl", " $playerState")
                }

            }
            mediaPlayer.start()
            playerState = PlayerState.PLAY
        }
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSE
    }

    override fun getPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun destroy() {
        mediaPlayer.release()
        playerState = PlayerState.DESTROY
    }


}