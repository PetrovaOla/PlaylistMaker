package petrova.ola.playlistmaker.presentation.player

sealed interface PlayerScreenState {

    data object Loading : PlayerScreenState

    data class Content(
        val playerState: PlayerState = PlayerState.INITIAL,
        val playerPos: Int = 0
    ) : PlayerScreenState

    data object Error : PlayerScreenState

}