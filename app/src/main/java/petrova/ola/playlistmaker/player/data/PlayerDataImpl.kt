package petrova.ola.playlistmaker.player.data

import android.media.MediaPlayer
import android.util.Log
import petrova.ola.playlistmaker.player.domain.PlayerData
import petrova.ola.playlistmaker.player.domain.PlayerState

class PlayerDataImpl(private val mediaPlayer: MediaPlayer) : PlayerData {

    private var playerState = PlayerState.INITIAL

    init {
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

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

}