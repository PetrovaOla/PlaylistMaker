package petrova.ola.playlistmaker.player.domain

enum class PlayerState {
    INITIAL,
    PREPARE,
    RESUME,
    PLAY,
    PAUSE,
    ERROR,
    DESTROY,
}