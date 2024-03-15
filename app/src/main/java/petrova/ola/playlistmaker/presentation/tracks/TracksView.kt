package petrova.ola.playlistmaker.presentation.tracks

import petrova.ola.playlistmaker.presentation.player.PlayerScreenState

interface TracksView {
    // Методы, меняющие внешний вид экрана

    fun render(state: PlayerScreenState)

    // Методы «одноразовых событий»

    fun showToast(additionalMessage: String)

}