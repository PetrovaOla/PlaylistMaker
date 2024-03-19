package petrova.ola.playlistmaker.search.ui

sealed interface ToastState {
    object None : ToastState
    data class Show(val additionalMessage: String) : ToastState
}