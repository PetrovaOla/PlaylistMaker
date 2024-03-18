package petrova.ola.playlistmaker.search.ui

import petrova.ola.playlistmaker.player.ui.PlayerScreenState

interface TracksView {
    // Методы, меняющие внешний вид экрана

    fun render(state: PlayerScreenState)

    // Методы «одноразовых событий»

    fun showToast(additionalMessage: String)

}