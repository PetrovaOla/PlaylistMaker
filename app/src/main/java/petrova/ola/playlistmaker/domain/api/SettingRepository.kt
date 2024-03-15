package petrova.ola.playlistmaker.domain.api

interface SettingRepository {
    fun saveTheme(value: Boolean)
    fun loadTheme(): Boolean
}