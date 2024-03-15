package petrova.ola.playlistmaker.presentation.tracks

sealed interface ToastState {
    object None : ToastState
    data class Show(val additionalMessage: String) : ToastState
}