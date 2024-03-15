package petrova.ola.playlistmaker.domain.api

interface SettingInteractor {
    fun loadTheme(): Boolean
    fun saveTheme(value: Boolean)
}