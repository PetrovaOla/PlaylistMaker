package petrova.ola.playlistmaker.setting.domain

interface SettingRepository {
    fun saveTheme(value: Boolean)
    fun loadTheme(): Boolean
}