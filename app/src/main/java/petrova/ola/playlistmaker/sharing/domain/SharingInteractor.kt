package petrova.ola.playlistmaker.sharing.domain

interface SharingInteractor {
    fun shareApp()
    fun userAgreement()
    fun openSupport()
    fun sharePlaylist(message: String)
}