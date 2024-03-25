package petrova.ola.playlistmaker.player.domain

interface PlayerData {
    fun setDataSource(url: String)
    fun playerState(): PlayerState
    fun getPosition(): Int
    fun start()
    fun pause()
    fun destroy()

}
