package petrova.ola.playlistmaker.player.domain

interface PlayerInteractor {
    fun playerState(): PlayerState
    fun start()
    fun pause()
    fun getPosition(): Int
    fun destroy()

}