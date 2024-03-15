package petrova.ola.playlistmaker.presentation.player

enum class PlayerState {
    INITIAL,
    PREPARE,
    RESUME,
    PLAY,
    PAUSE,
    ERROR,
    DESTROY,
}