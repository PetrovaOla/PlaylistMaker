package petrova.ola.playlistmaker.player.domain.impl

import petrova.ola.playlistmaker.player.domain.PlayerData
import petrova.ola.playlistmaker.player.domain.PlayerInteractor
import petrova.ola.playlistmaker.player.domain.PlayerState

class PlayerInteractorImpl(private val mediaPlayer: PlayerData) : PlayerInteractor {

    override fun playerState(): PlayerState {
        return mediaPlayer.playerState()
    }

    override fun start() {
        mediaPlayer.start()
    }

    override fun pause() {
        mediaPlayer.pause()
    }

    override fun getPosition(): Int {
        return mediaPlayer.getPosition()
    }

    override fun destroy() {
        mediaPlayer.destroy()
    }

    override fun setDataSource(url: String) {
        mediaPlayer.setDataSource(url)
    }

}
