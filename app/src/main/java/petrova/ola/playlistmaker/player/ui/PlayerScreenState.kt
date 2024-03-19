package petrova.ola.playlistmaker.player.ui

import petrova.ola.playlistmaker.player.domain.PlayerState

sealed interface PlayerScreenState {

    data object Loading : PlayerScreenState

    data class Content(
        val playerState: PlayerState = PlayerState.INITIAL,
        val playerPos: Int = 0
    ) : PlayerScreenState

    data object Error : PlayerScreenState

}