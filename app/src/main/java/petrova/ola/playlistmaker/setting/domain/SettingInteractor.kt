package petrova.ola.playlistmaker.setting.domain

interface SettingInteractor {
    fun loadTheme(): Boolean
    fun saveTheme(value: Boolean)
    fun applyTheme()
}