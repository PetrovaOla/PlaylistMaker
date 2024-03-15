package petrova.ola.playlistmaker.domain.api

import petrova.ola.playlistmaker.presentation.player.PlayerState

interface PlayerInteractor {
    fun playerState(): PlayerState
    fun start()
    fun pause()
    fun getPosition(): Int
    fun destroy()

}